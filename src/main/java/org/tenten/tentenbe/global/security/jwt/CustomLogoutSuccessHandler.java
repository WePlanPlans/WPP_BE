package org.tenten.tentenbe.global.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.token.exception.LogoutMemberException;
import org.tenten.tentenbe.global.security.jwt.model.RefreshToken;
import org.tenten.tentenbe.global.security.jwt.repository.RefreshTokenRepository;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그아웃 호출됐음");
//        setDefaultTargetUrl("https://weplanplans.site/api/auth/logout-redirect");
        setDefaultTargetUrl("http://localhost:5173/api/auth/logout-redirect");
//        setDefaultTargetUrl("http://localhost:8080/api/auth/logout-redirect");

        String token = request.getHeader("Authorization").split(" ")[1];
        log.info("token = " + token);

        // 엑세스 토큰을 사용하여 사용자 정보를 가져옴
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        User principal = (User) auth.getPrincipal();
        Long memberId = Long.valueOf(principal.getUsername());

        // 데이터베이스에서 리프레시 토큰 검증 및 삭제
        Optional<RefreshToken> refreshTokenEntityOptional = refreshTokenRepository.findByMember_Id(memberId);
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new LogoutMemberException("로그아웃 호출한 멤버가 데이터베이스에 없습니다."));
        if (refreshTokenEntityOptional.isPresent()) {
            member.getRefreshToken().updateToken(null);
        } else {
            throw new LogoutMemberException("리프레시 토큰이 데이터베이스에 없습니다.");
        }

        log.info(request.getRequestURI());
        super.onLogoutSuccess(request, response, authentication);
    }

}
