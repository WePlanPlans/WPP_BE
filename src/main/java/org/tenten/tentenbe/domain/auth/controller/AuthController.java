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
import org.tenten.tentenbe.domain.auth.dto.response.SignUpResponse;
import org.tenten.tentenbe.domain.auth.service.AuthService;
import org.tenten.tentenbe.global.response.GlobalDataResponse;

import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;
import static org.tenten.tentenbe.global.util.SecurityUtil.getCurrentMemberId;

@Tag(name = "인증 관련 API", description = "유저 인증 관련 API 모음입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입 API", description = "회원가입 API 입니다.")
    @PostMapping("/signup")
    public ResponseEntity<GlobalDataResponse<SignUpResponse>> signUp(
        @Valid @RequestBody SignUpRequest signUpRequest, HttpServletResponse response
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, authService.signUp(signUpRequest, response)));
    }

    @Operation(summary = "로그인-이메일 API", description = "로그인-이메일 API 입니다.")
    @PostMapping("/login")
    public ResponseEntity<GlobalDataResponse<LoginResponse>> login(
        @Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, authService.login(loginRequest, response)));
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃 API 입니다.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response, getCurrentMemberId());
        return ResponseEntity.ok("LOGOUT!");
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
