package org.tenten.tentenbe.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Tag(name = "인증 관련 API", description = "유저 인증 관련 API 모음입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입 API", description = "회원가입 API 입니다.")
    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        authService.signUp(null, signUpRequest);
        return ResponseEntity.ok(GlobalResponse.ok(SUCCESS));
    }

    @Operation(summary = "로그인-이메일 API", description = "로그인-이메일 API 입니다.")
    @PostMapping("/login")
    public ResponseEntity<GlobalDataResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, authService.login(null, loginRequest)));
    }

    @Operation(summary = "로그인-카카오 API", description = "로그인-카카오 API 입니다.")
    @PostMapping("/login/kakao")
    public ResponseEntity<GlobalDataResponse<LoginResponse>> loginKakao(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, authService.loginKakao(null, loginRequest)));
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃 API 입니다.")
    @PostMapping("/logout")
    public ResponseEntity<GlobalResponse> logout() {
        authService.logout(null);
        return ResponseEntity.ok(GlobalResponse.ok(SUCCESS));
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
