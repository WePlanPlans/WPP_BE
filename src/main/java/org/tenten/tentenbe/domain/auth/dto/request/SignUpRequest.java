package org.tenten.tentenbe.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.global.common.enums.LoginType;
import org.tenten.tentenbe.global.common.enums.UserAuthority;

public record SignUpRequest(
    @Email(message = "올바르지 않은 이메일 형식입니다.")
    @Schema(defaultValue = "example@mail.com")
    String email,

    @NotNull(message = "비밀번호는 최소 8글자 이상입니다.")
    @Size(min = 8, max = 20)
    @Schema(defaultValue = "as@#SD23/&DFd%fs@a1")
    String password

) {
    public Member toEntity(
        String encodedPassword, LoginType loginTypeEmail, UserAuthority userAuthority) {
        return Member.builder()
            .email(email)
            .password(encodedPassword)
            .userAuthority(userAuthority) //TODO : 권한 처리 논의
            .loginType(loginTypeEmail) // TODO : 로그인 타입 처리 논의
            .build();
    }
}