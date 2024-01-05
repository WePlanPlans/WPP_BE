package org.tenten.tentenbe.global.security.oauth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
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
import org.tenten.tentenbe.domain.auth.dto.request.LoginRequest;
import org.tenten.tentenbe.domain.auth.dto.response.LoginResponse;
import org.tenten.tentenbe.domain.auth.dto.response.MemberDto;
import org.tenten.tentenbe.domain.auth.service.AuthService;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.token.dto.TokenDTO;
import org.tenten.tentenbe.global.common.enums.UserAuthority;
import org.tenten.tentenbe.global.security.jwt.JwtTokenProvider;
import org.tenten.tentenbe.global.security.jwt.model.RefreshToken;
import org.tenten.tentenbe.global.security.jwt.repository.RefreshTokenRepository;
import org.tenten.tentenbe.global.util.CookieUtil;

import java.util.List;
import java.util.Map;

import static org.tenten.tentenbe.global.common.enums.LoginType.KAKAO;
import static org.tenten.tentenbe.global.common.enums.UserAuthority.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

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

        String email = (String) kakaoAccountValue.get("email");

        boolean isExist = memberRepository.existsByEmailAndLoginType(email, KAKAO);
        kakaoAccountValue.put("isExist", isExist);

        if(isExist) {
            //로그인 처리
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, "dummy");
            Authentication authenticate =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            TokenDTO.TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);
            log.info("로그인 API 중 토큰 생성 로직 실행");

            String memberId = authenticate.getName();
            Member member = memberRepository.findById(Long.parseLong(memberId)).orElseThrow(RuntimeException::new);

            String refreshToken = tokenInfoDTO.getRefreshToken();
            member.getRefreshToken().updateToken(refreshToken);

            kakaoAccountValue.put("member", member);
            kakaoAccountValue.put("tokenInfoDto", tokenInfoDTO);

        } else {
            //회원가입 처리
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode("dummy"))
                .loginType(KAKAO)
                .userAuthority(UserAuthority.ROLE_USER)
                .build();

            RefreshToken refreshToken = RefreshToken.builder()
                .member(member)
                .build();

            memberRepository.save(member);
            refreshTokenRepository.save(refreshToken);
        }

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
    }
}
