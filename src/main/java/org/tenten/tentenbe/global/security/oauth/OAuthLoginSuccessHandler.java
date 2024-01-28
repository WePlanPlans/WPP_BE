package org.tenten.tentenbe.global.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.token.dto.TokenDTO;
import org.tenten.tentenbe.global.cache.RedisCache;
import org.tenten.tentenbe.global.security.jwt.JwtTokenProvider;
import org.tenten.tentenbe.global.util.CookieUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.tenten.tentenbe.global.common.constant.TopicConstant.REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisCache redisCache;
    @Value("${oauth.redirectUrl}")
    private String redirectUrl;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        ObjectMapper mapper = new ObjectMapper();   //todo : ObjectMapper 공통 유틸로 분리하면 리팩토링 진행예정

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = defaultOAuth2User.getAttributes();
        Map<String, Object> kakaoAccountValue = (Map<String, Object>) attributes.get("kakao_account");

        String email = (String) kakaoAccountValue.get("email");

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, email); // 패스워드 : 해당 이메일 암호화
        Authentication authenticate =
            authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDTO.TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);

        String memberId = authenticate.getName();
        Member member = memberRepository.findById(Long.parseLong(memberId)).orElseThrow(RuntimeException::new);

        String refreshToken = tokenInfoDTO.getRefreshToken();
        redisCache.save(REFRESH_TOKEN, Long.toString(member.getId()), refreshToken);

        CookieUtil.storeRefreshTokenInCookie(response, refreshToken);

        boolean isExist = (boolean) kakaoAccountValue.get("isExist");

        String nickname = "";
        if (member.getNickname() != null) {
            nickname = URLEncoder.encode(member.getNickname(), StandardCharsets.UTF_8);
        }

        String redirectURI = buildRedirectURI(email, nickname, tokenInfoDTO, member.getProfileImageUrl(), isExist);
        response.sendRedirect(redirectURI);
    }

    private String buildRedirectURI(String email, String nickname, TokenDTO.TokenInfoDTO tokenInfoDTO, String profileImageUrl, boolean isExist) {
        return UriComponentsBuilder.fromUriString(redirectUrl)
            .queryParam("nickname", nickname)
            .queryParam("email", email)
            .queryParam("token", tokenInfoDTO.toTokenIssueDTO().getAccessToken())
            .queryParam("profile_image", profileImageUrl)
            .queryParam("signup", isExist)
            .build().toUriString();
    }
}
