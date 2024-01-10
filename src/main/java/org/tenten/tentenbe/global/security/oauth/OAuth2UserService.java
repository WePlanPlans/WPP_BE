package org.tenten.tentenbe.global.security.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.global.common.enums.AgeType;
import org.tenten.tentenbe.global.common.enums.GenderType;
import org.tenten.tentenbe.global.security.jwt.model.RefreshToken;
import org.tenten.tentenbe.global.security.jwt.repository.RefreshTokenRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.tenten.tentenbe.global.common.enums.LoginType.KAKAO;
import static org.tenten.tentenbe.global.common.enums.UserAuthority.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
//    private final AuthService authService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Role generate
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(ROLE_USER.name());

        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();


        Map<String, Object> kakaoAccountValue =
            (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
        Map<String, Object> profile =
            (Map<String, Object>) kakaoAccountValue.getOrDefault("profile", new HashMap<String, Object>());

        String email = (String) kakaoAccountValue.get("email");
        String genderType = (String) kakaoAccountValue.getOrDefault("gender", "");
        String ageType = (String) kakaoAccountValue.getOrDefault("age_range", "");

        String nickname = (String) profile.get("nickname");
        String profile_image = (String) profile.getOrDefault("thumbnail_image_url", "");

        GenderType genderEnum = extractGenderType(genderType);
        AgeType ageEnum = extractAgeType(ageType);

        // 닉네임 중복 체크 및 임의 생성

        boolean isExist = memberRepository.existsByEmailAndLoginType(email, KAKAO);

        if(!isExist) {
            //회원가입 처리
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            //todo: passwordEncoder를 Security가 아닌 새로운 Config에 빈 등록

            Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode("dummy"))    //todo: OAuth 패스워드 환경 변수로 빼기
                .nickname(nickname)
                .genderType(genderEnum)
                .ageType(ageEnum)
                .profileImageUrl(profile_image)
                .loginType(KAKAO)
                .userAuthority(ROLE_USER)
                .build();
            RefreshToken refreshToken = RefreshToken.builder()
                .member(member)
                .build();

            memberRepository.save(member);
            refreshTokenRepository.save(refreshToken);
        }

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
    }

    // 성별 추출
    private static GenderType extractGenderType(String genderType) {
        GenderType genderEnum = GenderType.DEFAULT; // 기본값 설정

        if ("female".equalsIgnoreCase(genderType)) {
            genderEnum = GenderType.FEMALE;
        } else if ("male".equalsIgnoreCase(genderType)) {
            genderEnum = GenderType.MALE;
        }
        return genderEnum;
    }

    // 연령대 추출
    private static AgeType extractAgeType(String ageType) {
        log.info("ageType={}", ageType);
        AgeType ageEnum = AgeType.DEFATULT;

        if (ageType != null && ageType.length() >= 1) {
            char firstChar = ageType.charAt(0);
            switch (firstChar) {
                case '2':
                    ageEnum = AgeType.TWENTIES;
                    break;
                case '3':
                    ageEnum = AgeType.THIRTIES;
                    break;
                case '4':
                    ageEnum = AgeType.FOURTIES;
                    break;
                case '5':
                    ageEnum = AgeType.ABOVE_FIFTIES;
                    break;
                default:
                    ageEnum = AgeType.DEFATULT;
                    break;
            }
        }
        return ageEnum;
    }
}
