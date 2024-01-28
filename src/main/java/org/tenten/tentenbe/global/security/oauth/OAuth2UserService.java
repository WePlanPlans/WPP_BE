package org.tenten.tentenbe.global.security.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.config.PasswordEncoderConfig;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;

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
    private final PasswordEncoderConfig passwordEncoderConfig;

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
        String nickname = (String) profile.get("nickname");
        String profile_image = (String) profile.getOrDefault("thumbnail_image_url", "");

        boolean nicknameCheck = memberRepository.existsByNickname(nickname);
        boolean isExist = memberRepository.existsByEmailAndLoginType(email, KAKAO);

        kakaoAccountValue.put("isExist", isExist);

        //회원가입 처리
        if(!isExist) {

            Member member = Member.builder()
                .email(email)
                .password(passwordEncoderConfig.passwordEncoder().encode(email))
                .nickname(nickname)
                .profileImageUrl(profile_image)
                .loginType(KAKAO)
                .userAuthority(ROLE_USER)
                .build();

            memberRepository.save(member);

            if (nicknameCheck) {
                nickname = "위플러" + (member.getId() + 321);
                member.updateNickname(nickname);
            }
        }
        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
    }
}
