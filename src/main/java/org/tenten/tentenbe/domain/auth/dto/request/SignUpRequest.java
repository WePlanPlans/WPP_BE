package org.tenten.tentenbe.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.model.Survey;
import org.tenten.tentenbe.global.common.enums.AgeType;
import org.tenten.tentenbe.global.common.enums.GenderType;
public record SignUpRequest(
    @Email
    @Schema(description = "회원가입 이메일", defaultValue = "example@mail.com")
    String email,

    @NotNull
    @Size(min = 8)
    @Schema(description = "회원가입 비밀번호", defaultValue = "as@#SD23/&DFd%fs@a1")
    String password,

    @NotNull
    @Schema(description = "이름", defaultValue = "name")
    String name,

    @NotNull
    @Size(min = 2, max = 19)
    @Schema(description = "닉네임", defaultValue = "nickName")
    String nickname,

    @Schema(description = "성별", defaultValue = "genderType")
    GenderType genderType,

    @Schema(description = "연령대", defaultValue = "ageType")
    AgeType ageType,

    @Schema(description = "프로필 이미지", defaultValue = "http://~~~~~~image.jpg")
    String profileImage,

    @Schema(description = "설문조사 결과", defaultValue = "{}")
    Survey survey
) {
    public Member toEntity(String encodedPassword) {
        return Member.builder()
            .email(email)
            .name(name)
            .password(encodedPassword)
            .nickname(nickname)
            .profileImageUrl(profileImage)
            .survey(survey)
            .ageType(ageType)
            .genderType(genderType)
//            .loginType() // TODO : 로그인 타입을 이넘으로 만들것인지 논의
            .build();
    }
}