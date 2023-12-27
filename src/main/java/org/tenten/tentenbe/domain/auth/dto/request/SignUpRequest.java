package org.tenten.tentenbe.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.domain.member.model.Survey;
import org.tenten.tentenbe.global.common.enums.AgeType;
import org.tenten.tentenbe.global.common.enums.GenderType;
public record SignUpRequest(
    @Schema(defaultValue = "example@mail.com")
    String email,

    @Schema(defaultValue = "as@#SD23/&DFd%fs@a1")
    String password,

    @Schema(defaultValue = "이름")
    String name,

    @Schema(defaultValue = "닉네임")
    String nickname,

    @Schema(defaultValue = "성별")
    GenderType genderType,

    @Schema(defaultValue = "연령대")
    AgeType ageType,

    @Schema(defaultValue = "프로필 이미지")
    String profileImage,

    @Schema(defaultValue = "설문조사 결과")
    Survey survey
    ) {

}