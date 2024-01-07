package org.tenten.tentenbe.global.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.tenten.tentenbe.domain.auth.dto.response.LoginResponse;
import org.tenten.tentenbe.domain.auth.dto.response.MemberDto;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.token.dto.TokenDTO;
import org.tenten.tentenbe.global.common.enums.UserAuthority;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.security.jwt.JwtTokenProvider;
import org.tenten.tentenbe.global.security.jwt.model.RefreshToken;
import org.tenten.tentenbe.global.security.jwt.repository.RefreshTokenRepository;
import org.tenten.tentenbe.global.util.CookieUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;
import static org.tenten.tentenbe.global.common.enums.LoginType.KAKAO;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        ObjectMapper mapper = new ObjectMapper();   //todo : Config에 Bean으로 따로 빼서 사용하던지,,,고려해서 리팩토링 할 예정

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = defaultOAuth2User.getAttributes();
        Map<String, Object> kakaoAccountValue = (Map<String, Object>) attributes.get("kakao_account");

        String email = (String) kakaoAccountValue.get("email"); //필수
        String age_range =(String) kakaoAccountValue.get("age_range");
        String gender= (String) kakaoAccountValue.get("gender");

        Map<String, Object> profile = (Map<String, Object>) kakaoAccountValue.getOrDefault("profile", new HashMap<String, Object>());

        String nickname = (String) profile.getOrDefault("nickname", ""); //필수
        String profile_image = (String) profile.get("thumbnail_image_url");

        boolean isExist = (boolean) kakaoAccountValue.get("isExist");

        // 로그인 처리
        if(isExist) {

            TokenDTO.TokenInfoDTO tokenInfoDTO =
                (TokenDTO.TokenInfoDTO) kakaoAccountValue.get("tokenInfoDto");
            String refreshToken = tokenInfoDTO.getRefreshToken();
            log.info("OAuthLoginSuccessHandler : refreshToken={}", refreshToken);
            CookieUtil.storeRefreshTokenInCookie(response, refreshToken);

            Member member = (Member) kakaoAccountValue.get("member");

            LoginResponse loginResponse =
                LoginResponse.builder()
                    .memberDto(MemberDto.fromEntity(member))
                    .tokenInfo(tokenInfoDTO.toTokenIssueDTO())
                    .build();

            //응답 포맷
            String loginGlobalResponseJsonString = mapper.writeValueAsString(ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, loginResponse)));
            response.getWriter().write(loginGlobalResponseJsonString);

//      회원가입 처리
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("http://localhost:8080")
//                .append("?name=").append(URLEncoder.encode(name, StandardCharsets.UTF_8))
                .append("?nickname=").append(URLEncoder.encode(nickname, StandardCharsets.UTF_8))
                .append("&email=").append(email)
                .append("&gender=").append(gender)
                .append("&age_range=").append(age_range)
                .append("&profile_image=").append(profile_image);

//
//            String redirectURI = sb.toString();
//            response.sendRedirect(redirectURI);
        }
    }
}
