package org.tenten.tentenbe.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.domain.member.model.Survey;
import org.tenten.tentenbe.global.common.enums.AgeType;
import org.tenten.tentenbe.global.common.enums.GenderType;
import org.tenten.tentenbe.global.common.enums.LoginType;

public record MemberResponse(
    @Schema(defaultValue = "1")
    Long memberId,
    @Schema(defaultValue = "닉네임")
    String nickname,
    @Schema(defaultValue = "example@mail.com")
    String email,
    @Schema(defaultValue = "프로필 이미지")
    String profileImageUrl,
    @Schema(defaultValue = "연령대")
    AgeType ageType,
    @Schema(defaultValue = "성별")
    GenderType genderType,
    @Schema(defaultValue = "설문조사 결과")
    Survey survey,
    @Schema(defaultValue = "로그인 타입")
    LoginType loginType
) {
}
