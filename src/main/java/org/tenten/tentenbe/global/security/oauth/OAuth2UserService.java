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
        String userNameAttributeName = getUserNameAttributeName(userRequest);

        Map<String, Object> kakaoAccountValue = getAttributeValue(oAuth2User.getAttributes(), "kakao_account");
        Map<String, Object> profile = getAttributeValue(kakaoAccountValue, "profile");

        String email = getStringValue(kakaoAccountValue, "email");
        String nickname = getStringValue(profile, "nickname");
        String profile_image = getStringValue(profile, "thumbnail_image_url");

        boolean isNicknameExist = memberRepository.existsByNickname(nickname);
        boolean isMemberExist = memberRepository.existsByEmailAndLoginType(email, KAKAO);
        kakaoAccountValue.put("isExist", isMemberExist);

        // 첫 로그인 -> 회원가입 처리
        if (!isMemberExist) {
            registerNewUser(email, nickname, profile_image, isNicknameExist);
        }

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
    }

    private String getUserNameAttributeName(OAuth2UserRequest userRequest) {
        return userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();
    }

    private void registerNewUser(String email, String nickname, String profileImage, boolean isNicknameExist) {
        Member member = Member.builder()
            .email(email)
            .password(passwordEncoderConfig.passwordEncoder().encode(email))
            .nickname(nickname)
            .profileImageUrl(profileImage)
            .loginType(KAKAO)
            .userAuthority(ROLE_USER)
            .build();

        memberRepository.save(member);

        if (isNicknameExist) {
            nickname = "위플러" + (member.getId() + 321);
            member.updateNickname(nickname);
        }
    }

    private Map<String, Object> getAttributeValue(Map<String, Object> attributes, String key) {
        return (Map<String, Object>) attributes.getOrDefault(key, new HashMap<>());
    }

    private String getStringValue(Map<String, Object> map, String key) {
        return (String) map.getOrDefault(key, "");
    }
}
