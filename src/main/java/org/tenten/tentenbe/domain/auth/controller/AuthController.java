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
import org.tenten.tentenbe.domain.auth.dto.response.LoginResponse;
import org.tenten.tentenbe.domain.auth.dto.response.SignUpResponse;
import org.tenten.tentenbe.domain.auth.service.AuthService;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.response.GlobalResponse;

import static org.tenten.tentenbe.global.common.constant.ResponseConstant.CREATED;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;


//1. 회원가입 (POST /api/auth/signup)
//2. 로그인-이메일 (POST /api/auth/login)
//3. 로그인-카카오 (POST /api/auth/login/kakao)
//4. 로그아웃 (POST /api/auth/logout)
//5. 비밀번호 재설정(PUT /api/auth/reset-password)

@Tag(name = "인증 관련 API", description = "유저 인증 관련 API 모음입니다.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입 API", description = "회원가입 API 입니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공시", content = @Content(schema = @Schema(implementation = SignUpResponse.class)))
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, authService.signUp(null, signUpRequest)));
    }

    @Operation(summary = "로그인-이메일 API", description = "로그인-이메일 API 입니다.")
    @ApiResponse(responseCode = "200", description = "이메일 로그인 성공시", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, authService.login(null, loginRequest)));
    }

    @Operation(summary = "로그인-카카오 API", description = "로그인-카카오 API 입니다.")
    @ApiResponse(responseCode = "200", description = "카카오 로그인 성공시", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @PostMapping("/login/kakao")
    public ResponseEntity<?> loginKakao(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, authService.loginKakao(null, loginRequest)));
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃 API 입니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공시")
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout(null);
        return ResponseEntity.ok(GlobalResponse.ok(SUCCESS));
    }

}
