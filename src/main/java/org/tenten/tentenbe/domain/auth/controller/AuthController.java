package org.tenten.tentenbe.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tenten.tentenbe.domain.auth.dto.request.LoginRequest;
import org.tenten.tentenbe.domain.auth.dto.request.SignUpRequest;
import org.tenten.tentenbe.domain.auth.dto.response.CheckResponse;
import org.tenten.tentenbe.domain.auth.dto.response.LoginResponse;
import org.tenten.tentenbe.domain.auth.service.AuthService;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.response.GlobalResponse;
import org.tenten.tentenbe.global.util.CookieUtil;

import static org.tenten.tentenbe.global.common.constant.JwtConstants.REFRESH_TOKEN_COOKIE_NAME;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Tag(name = "인증 관련 API", description = "유저 인증 관련 API 모음입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입 API", description = "회원가입 API 입니다.")
    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signUp(signUpRequest);
        return ResponseEntity.ok(GlobalResponse.ok(SUCCESS));
    }

    @Operation(summary = "로그인-이메일 API", description = "로그인-이메일 API 입니다.")
    @PostMapping("/login")
    public ResponseEntity<GlobalDataResponse<LoginResponse>> login(
        @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, authService.login(loginRequest, response)));
    }

    @Operation(summary = "로그인-카카오 API", description = "로그인-카카오 API 입니다.")
    @PostMapping("/login/kakao")
    public ResponseEntity<GlobalDataResponse<LoginResponse>> loginKakao(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, authService.loginKakao(null, loginRequest)));
    }

    @Operation(summary = "로그아웃시 리다이렉트 API", description = "로그아웃시 호출되는 API 입니다. Url = /logout")
    @GetMapping(value = "/logout-redirect")
    public ResponseEntity<String> logoutRedirect(@RequestBody HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME); // 쿠키 삭제
        return ResponseEntity.ok("LOGOUT");
    }

    @Operation(summary = "닉네임 중복 조회 API", description = "닉네임 중복 조회 API 입니다.")
    @GetMapping("/nicknames/check/{nickname}")
    public ResponseEntity<GlobalDataResponse<CheckResponse>> checkNickname(@PathVariable("nickname") String nickname) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, authService.nicknameCheck(nickname)));
    }

    @Operation(summary = "이메일 중복 조회 API", description = "이메일 중복 조회 API 입니다.")
    @GetMapping("/emails/check/{email}")
    public ResponseEntity<GlobalDataResponse<CheckResponse>> checkEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, authService.emailCheck(email)));
    }
}
