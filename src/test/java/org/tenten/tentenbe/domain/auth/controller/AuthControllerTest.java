package org.tenten.tentenbe.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.tenten.tentenbe.domain.auth.dto.response.CheckResponse;
import org.tenten.tentenbe.domain.auth.service.AuthService;
import org.tenten.tentenbe.domain.fixture.AuthFixture;
import org.tenten.tentenbe.domain.member.service.MemberService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.tenten.tentenbe.global.common.constant.JwtConstants.ACCESS_TOKEN_EXPIRE_TIME;
import static org.tenten.tentenbe.global.common.constant.JwtConstants.BEARER_TYPE;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("일반 회원가입 성공")
    public void signUpSuccess() throws Exception {

        //given
        given(authService.signUp(any(), any())).willReturn(AuthFixture.signUpResponse());

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(AuthFixture.signUpRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memberId").value(1L))
                .andExpect(jsonPath("$.data.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data.nickName").value("test"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입시 닉네임 중복 확인")
    public void checkNickName() throws Exception {
        //given
        given(authService.nicknameCheck(any())).willReturn(new CheckResponse(true));

        mockMvc.perform(get("/api/auth/nicknames/check/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입시 이메일 중복 확인")
    public void checkEmailAddress() throws Exception {
        //given
        given(authService.emailCheck(any())).willReturn(new CheckResponse(true));

        mockMvc.perform(get("/api/auth/emails/check/test@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("일반 이메일 로그인")
    public void logInWithEmail() throws Exception {

        //given
        given(authService.login(any(), any())).willReturn(AuthFixture.loginResponse());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(AuthFixture.signUpResponse())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memberDto.id").value(1L))
                .andExpect(jsonPath("$.data.memberDto.nickName").value("test"))
                .andExpect(jsonPath("$.data.memberDto.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data.tokenInfo.accessToken").value("c2lsdmVfijhtroiehJ432NwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK"))
                .andExpect(jsonPath("$.data.tokenInfo.grantType").value(BEARER_TYPE))
                .andExpect(jsonPath("$.data.tokenInfo.accessTokenExpiresIn").value(ACCESS_TOKEN_EXPIRE_TIME))
                .andDo(print());
    }


    @Test
    @DisplayName("소셜 카카오 로그인")
    public void signUpSuccessWithKakao() throws Exception {
        //given
        //TODO: 카카오 로그인 코드 완료 후 재 검토
        mockMvc.perform(post("/api/auth/login/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(AuthFixture.loginRequest())))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user", password = "password", roles = "USER")
    @DisplayName("로그아웃 성공")
    public void logOutSuccess() throws Exception {
        //given
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("LOGOUT!"))
                .andDo(print());
    }

}