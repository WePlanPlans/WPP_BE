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
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.auth.dto.response.MemberDto;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.token.dto.TokenDTO;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.security.jwt.JwtTokenProvider;

import java.io.IOException;
import java.util.Map;

import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        ObjectMapper mapper = new ObjectMapper();   //todo : Config에 Bean으로 따로 빼서 사용하던지,,,고려해서 리팩토링 할 예정

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = defaultOAuth2User.getAttributes();
        Map<String, Object> kakaoAccountValue = (Map<String, Object>) attributes.get("kakao_account");

        String email = (String) kakaoAccountValue.get("email");

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, "dummy");    //todo: 패스워드 환경변수 분리
        Authentication authenticate =
            authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDTO.TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);

        String memberId = authenticate.getName();
        Member member = memberRepository.findById(Long.parseLong(memberId)).orElseThrow(RuntimeException::new);

        String refreshToken = tokenInfoDTO.getRefreshToken();
        member.getRefreshToken().updateToken(refreshToken);

        OAuthLoginResponse oAuthLoginResponse =
            OAuthLoginResponse.builder()
                .memberDto(MemberDto.fromEntity(member))
                .tokenInfo(tokenInfoDTO.toTokenIssueDTO())
                .redirectUrl("https://weplanplans.vercel.app/")
                .build();

        String loginGlobalResponseJsonString = mapper.writeValueAsString(ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, oAuthLoginResponse)));
        response.getWriter().write(loginGlobalResponseJsonString);
    }
}