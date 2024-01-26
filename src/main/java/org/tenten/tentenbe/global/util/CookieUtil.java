package org.tenten.tentenbe.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

import static org.tenten.tentenbe.global.common.constant.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;
import static org.tenten.tentenbe.global.common.constant.JwtConstants.REFRESH_TOKEN_EXPIRE_TIME;

@Slf4j
public class CookieUtil {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }


    // 리프레시 토큰을 http only 쿠키에 저장 및 secure 설정
    public static void storeRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .sameSite("None")
            .maxAge(REFRESH_TOKEN_EXPIRE_TIME)
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    ResponseCookie responseCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .sameSite("None")
                        .maxAge(0)
                        .build();
                    response.addHeader("Set-Cookie", responseCookie.toString());
                }
            }
        }
    }

}
