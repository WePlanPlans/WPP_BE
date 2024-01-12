package org.tenten.tentenbe.domain.fixture;

import org.tenten.tentenbe.domain.auth.dto.request.LoginRequest;
import org.tenten.tentenbe.domain.auth.dto.request.SignUpRequest;
import org.tenten.tentenbe.domain.auth.dto.response.LoginResponse;
import org.tenten.tentenbe.domain.auth.dto.response.MemberDto;
import org.tenten.tentenbe.domain.auth.dto.response.SignUpResponse;
import org.tenten.tentenbe.domain.member.dto.request.MemberUpdateRequest;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.model.Survey;
import org.tenten.tentenbe.domain.token.dto.TokenDTO;
import org.tenten.tentenbe.global.common.enums.GenderType;
import org.tenten.tentenbe.global.common.enums.LoginType;
import org.tenten.tentenbe.global.security.jwt.model.RefreshToken;

import java.sql.Timestamp;
import java.time.Instant;

import static org.tenten.tentenbe.domain.token.dto.TokenDTO.*;
import static org.tenten.tentenbe.global.common.constant.JwtConstants.ACCESS_TOKEN_EXPIRE_TIME;
import static org.tenten.tentenbe.global.common.constant.JwtConstants.BEARER_TYPE;
import static org.tenten.tentenbe.global.common.enums.AgeType.TEENAGER;
import static org.tenten.tentenbe.global.common.enums.GenderType.DEFAULT;
import static org.tenten.tentenbe.global.common.enums.LoginType.EMAIL;
import static org.tenten.tentenbe.global.common.enums.UserAuthority.ROLE_USER;

public class AuthFixture {

    public static SignUpRequest signUpRequest(){
        return new SignUpRequest("test@gmail.com", "qwerty1234");
    }

    public static SignUpResponse signUpResponse(){
        return new SignUpResponse(1L, "test@gmail.com", "test", null);
    }


    public static LoginRequest loginRequest(){
        return new LoginRequest("test@gmail.com", "qwerty1234");
    }

    public static LoginResponse loginResponse(){
        return LoginResponse.builder()
                .memberDto(
                        MemberDto.builder()
                                .id(1L)
                                .nickName("test")
                                .email("test@gmail.com")
                                .build()
                )
                .tokenInfo(TokenDTO.TokenIssueDTO.builder()
                        .accessToken("c2lsdmVfijhtroiehJ432NwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK")
                        .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRE_TIME)
                        .grantType(BEARER_TYPE)
                        .build())
                .build();
    }

    public static Member newBasicMember(){
        return Member.builder()
                .id(1L)
                .ageType(TEENAGER)
                .loginType(LoginType.EMAIL)
                .nickname("nickNameTest")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .profileImageUrl("naver.com")
                .userAuthority(ROLE_USER)
                .email("test@gmail.com")
                .survey(new Survey("계획성", "활동성", "숙소", "음식", "여행 스타일"))
                .build();
    }




    public static Member updateMember(){
        return Member.builder()
                .id(1L)
                .ageType(TEENAGER)
                .loginType(LoginType.EMAIL)
                .genderType(GenderType.DEFAULT)
                .nickname("update my nickName")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .profileImageUrl("updateNaver.com")
                .userAuthority(ROLE_USER)
                .email("test@gmail.com")
                .build();
    }
    public static Member newMember(){
        return signUpRequest().toEntity("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm", EMAIL, ROLE_USER);
    }

    public static RefreshToken refreshToken(){
        return RefreshToken.builder()
                .member(Member.builder()
                        .id(1L)
                        .ageType(TEENAGER)
                        .loginType(LoginType.EMAIL)
                        .nickname("test")
                        .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                        .profileImageUrl("naver.com")
                        .userAuthority(ROLE_USER)
                        .email("test@gmail.com")
                        .build())
                .token("c2lsdmVfijhtroiehJ432NwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK")
                .build();
    }

    public static MemberUpdateRequest memberUpdateRequest(){
        return new MemberUpdateRequest("update my nickName","updateNaver.com",TEENAGER,DEFAULT);
    }
}
