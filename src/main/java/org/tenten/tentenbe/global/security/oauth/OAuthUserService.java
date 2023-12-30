package org.tenten.tentenbe.global.security.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.member.service.MemberService;
import org.tenten.tentenbe.global.common.enums.GenderType;
import org.tenten.tentenbe.global.common.enums.UserAuthority;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.tenten.tentenbe.global.common.enums.LoginType.KAKAO;
import static org.tenten.tentenbe.global.common.enums.UserAuthority.ROLE_USER;

@Service
@RequiredArgsConstructor
public class OAuthUserService extends DefaultOAuth2UserService {
    private MemberRepository memberRepository;
    private MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // OAuth2UserService를 사용하여 OAuth2User 정보를 가져온다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 클라이언트 등록 ID(google, naver, kakao)와 사용자 이름 속성을 가져온다.
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String attributeKey = userRequest.getClientRegistration()
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        System.out.println("AttributeKey : " + attributeKey);

        assert (provider.equals("kakao"));

        Map<String, Object> kakaoUserAttributes =
            (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");

        String email = (String) kakaoUserAttributes.get("account_email");
        String userName = (String) kakaoUserAttributes.get("name");
        String nickName = (String) kakaoUserAttributes.get("profile_nickname");
        String genderType = (String) kakaoUserAttributes.get("gender");
        String ageType = (String) kakaoUserAttributes.get("age_range");

        System.out.println("kakao Email : " + email);
        System.out.println("kakao userName : " + userName);
        System.out.println("kakao nickName : " + nickName);

        Optional<Member> findMember = memberRepository.findByEmailAndLoginType(email, KAKAO);

        if(findMember.isEmpty()) {
            /* 처음 소셜 로그인 해서 회원가입 해야하는 상황 */

            kakaoUserAttributes.put("isExist", false);

            return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(ROLE_USER.name())),
                kakaoUserAttributes, "account_email"
            );
        }

        kakaoUserAttributes.put("isExist", true);
        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(findMember.get().getUserAuthority().name())),
            kakaoUserAttributes, "account_email"
        );


    }
}
