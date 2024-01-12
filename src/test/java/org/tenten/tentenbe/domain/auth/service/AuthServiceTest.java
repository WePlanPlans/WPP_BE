package org.tenten.tentenbe.domain.auth.service;


import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.tenten.tentenbe.common.WithMockCustomUser;
import org.tenten.tentenbe.domain.auth.dto.request.SignUpRequest;
import org.tenten.tentenbe.domain.auth.dto.response.CheckResponse;
import org.tenten.tentenbe.domain.auth.dto.response.LoginResponse;
import org.tenten.tentenbe.domain.auth.dto.response.SignUpResponse;
import org.tenten.tentenbe.domain.fixture.AuthFixture;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.global.common.enums.LoginType;
import org.tenten.tentenbe.global.common.enums.UserAuthority;
import org.tenten.tentenbe.global.security.jwt.repository.RefreshTokenRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    @DisplayName("일반 회원가입 성공")
    public void signUpSuccess() throws Exception {

        SignUpRequest signUpRequest = new SignUpRequest("test@gmail.com", "qwerty1234");
        Member newMember = signUpRequest.toEntity(passwordEncoder.encode(signUpRequest.password()), LoginType.EMAIL, UserAuthority.ROLE_USER);

        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(memberRepository.save(any())).willReturn(newMember);
        given(memberRepository.findByEmail(any())).willReturn(Optional.ofNullable(AuthFixture.newMember()));
        given(refreshTokenRepository.save(any())).willReturn(AuthFixture.refreshToken());
        given(passwordEncoder.encode(any())).willReturn("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm");
        //when
        SignUpResponse signUpResponse = authService.signUp(AuthFixture.signUpRequest(), httpServletResponse);
        assertThat(signUpResponse).extracting("memberId", "email", "nickName")
                .containsExactly(1L, "test@gmail.com", "test");
    }

    @Test
    @DisplayName("로그인시 토큰 발급 성공")
    public void tokenIssuedSuccess() throws Exception {

//        TODO: "this.authenticationManagerBuilder" is null 오류

        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(AuthFixture.newMember()));
        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(memberRepository.save(any())).willReturn(AuthFixture.newMember());

        LoginResponse loginResponse = authService.login(AuthFixture.loginRequest(), httpServletResponse);

        assertNotNull(loginResponse);

    }

    @Test
    @WithMockCustomUser()
    @DisplayName("로그아웃 성공")
    public void logOutSuccess() throws Exception {

        //TODO: There is no PasswordEncoder mapped for the id "null" 오류
    }

    @Test
    @DisplayName("회원가입시 이메일 중복 확인")
    public void checkEmailAddress() throws Exception {
        given(memberRepository.existsByEmail(any(String.class))).willReturn(true);
        CheckResponse checkResponse = authService.emailCheck("test@gmail.com");
        assertThat(checkResponse.exists()).isTrue();
    }

    @Test
    @DisplayName("회원가입시 닉네임 중복 확인")
    public void checkNickName() throws Exception {
        given(memberRepository.existsByNickname(any(String.class))).willReturn(false);
        CheckResponse checkResponse = authService.nicknameCheck("test2");
        assertThat(checkResponse.exists()).isFalse();

    }


    @Test
    @DisplayName("소셜 카카오 로그인")
    public void signUpSuccessWithKakao() throws Exception {
        //TODO:구현예정
    }


}
