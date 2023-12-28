package org.tenten.tentenbe.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.tenten.tentenbe.domain.token.exception.ExpireAccessTokenException;
import org.tenten.tentenbe.global.response.ErrorResponse;
import org.tenten.tentenbe.global.security.jwt.JwtTokenProvider;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.tenten.tentenbe.global.common.constant.JwtConstants.AUTHORIZATION_HEADER;
import static org.tenten.tentenbe.global.common.constant.JwtConstants.BEARER_PREFIX;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        log.info(request.getMethod()+" "+ request.getRequestURI());
        try {
            String accessToken = extractTokenFromRequest(request);

            if (accessToken != null) {
                if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
                    // validateToken으로 유효성 검사 후 정상 토큰이면, Authentication을 가져와서 SecurityContext에 저장
                    Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.info("Access Token 만료!");
                    throw new ExpireAccessTokenException("Access Token 만료!");
                }
            }
            filterChain.doFilter(request, response);

        } catch (ExpireAccessTokenException e) {
            logException(e);
            String result = mapper.writeValueAsString(new ErrorResponse(SC_UNAUTHORIZED, e.getMessage()));
            response.setStatus(response.SC_UNAUTHORIZED);
            setResponse(response);
            try {
                response.getWriter().write(result);
            } catch (IOException exception) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            logException(e);
            e.printStackTrace();
            String result = mapper.writeValueAsString(new ErrorResponse(SC_INTERNAL_SERVER_ERROR, e.getMessage()));
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
            setResponse(response);
            try {
                response.getWriter().write(result);
            } catch (IOException exception) {
                e.printStackTrace();
            }
        }

    }

    private static void setResponse(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin", "https://weplanplans.site");
    }

    private static void logException(Exception e) {
        log.info(e.getMessage());
        log.info(e.getClass().getName());
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }
}
