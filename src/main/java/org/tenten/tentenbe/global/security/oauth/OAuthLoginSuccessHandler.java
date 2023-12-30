package org.tenten.tentenbe.global.security.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.tenten.tentenbe.domain.token.dto.TokenDTO;
import org.tenten.tentenbe.domain.token.dto.TokenDTO.TokenInfoDTO;
import org.tenten.tentenbe.global.security.jwt.JwtTokenProvider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private JwtTokenProvider jwtTokenProvider;


    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = (String) oAuth2User.getAttribute("account_email");
        String username = (String) oAuth2User.getAttribute("name");
        boolean isExist = oAuth2User.getAttribute("isExist");


        String redirectUrl = "";
        if(isExist) {
            TokenInfoDTO token = jwtTokenProvider.generateTokenDto(authentication);

            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/oauth/loginSuccess")
                .queryParam("accessToken", token.getAccessToken())
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
            log.info("redirect 준비");
        }
        else {
            //회원 가입 해야하는 상황
            redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/oauth/joinForm")
                .queryParam("email", email)
                .queryParam("username", username)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
        }

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }
}
