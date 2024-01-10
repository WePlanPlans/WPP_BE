package org.tenten.tentenbe.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.tenten.tentenbe.global.response.ErrorResponse;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info(String.valueOf(authException.getClass()));
        log.info(authException.getMessage());
        sendResponse(response, authException);
    }

    private void sendResponse(HttpServletResponse response, AuthenticationException authException) throws IOException {
        String result = "서버 에러가 발생했습니다";
        if (authException instanceof BadCredentialsException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(409, "잘못된 이메일, 비밀번호 입니다."));
            response.setStatus(response.SC_CONFLICT);
        } else if (authException instanceof InternalAuthenticationServiceException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(404,
                    "존재하지 않는 멤버입니다."));
            response.setStatus(response.SC_NOT_FOUND);
        } else if (authException instanceof InsufficientAuthenticationException){
            result = objectMapper.writeValueAsString(new ErrorResponse(401, "Access Token이 존재하지 않습니다."));
            response.setStatus(response.SC_UNAUTHORIZED);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin","http://localhost:5173");
        response.getWriter().write(result);
    }
}


