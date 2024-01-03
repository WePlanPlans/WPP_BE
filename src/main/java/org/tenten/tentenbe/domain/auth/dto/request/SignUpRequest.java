package org.tenten.tentenbe.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.model.Survey;
import org.tenten.tentenbe.global.common.enums.AgeType;
import org.tenten.tentenbe.global.common.enums.GenderType;
import org.tenten.tentenbe.global.common.enums.LoginType;
import org.tenten.tentenbe.global.common.enums.UserAuthority;

public record SignUpRequest(
    @Email(message = "올바르지 않은 이메일 형식입니다.")
    @Schema(defaultValue = "example@mail.com")
    String email,

    @NotNull(message = "비밀번호는 최소 8글자 이상입니다.")
    @Size(min = 8, max = 20)
    @Schema(defaultValue = "as@#SD23/&DFd%fs@a1")
    String password,

    @NotNull(message = "이름을 입력해주세요.")
    @Schema(defaultValue = "John")
    String name,

    @NotNull(message = "닉네임은 2글자 이상 20글자 이하입니다.")
    @Size(min = 2, max = 20)
    @Schema(defaultValue = "johnnyiq")
    String nickname,

    @Schema(defaultValue = "MALE")
    @Enumerated(EnumType.STRING)
    GenderType genderType,

    @Schema(defaultValue = "TEENAGER")
    @Enumerated(EnumType.STRING)
    AgeType ageType,

    @Schema(defaultValue = "http://asiduheimage.jpg")
    String profileImage,

    @Schema(defaultValue = "설문조사")
    Survey survey

) {
    public Member toEntity(
        String encodedPassword, LoginType loginTypeEmail, UserAuthority userAuthority) {
        return Member.builder()
            .email(email)
            .name(name)
            .password(encodedPassword)
            .nickname(nickname)
            .profileImageUrl(profileImage)
            .survey(survey)
            .ageType(ageType)
            .genderType(genderType)
            .userAuthority(userAuthority) //TODO : 권한 처리 논의
            .loginType(loginTypeEmail) // TODO : 로그인 타입 처리 논의
            .build();
    }
}