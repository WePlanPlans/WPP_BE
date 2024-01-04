package org.tenten.tentenbe.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.domain.member.model.Survey;
import org.tenten.tentenbe.global.common.enums.AgeType;
import org.tenten.tentenbe.global.common.enums.GenderType;

public record MemberUpdateRequest(
    @Schema(defaultValue = "닉네임")
    String nickname,
    @Schema(defaultValue = "프로필 이미지")
    String profileImageUrl,
    @Schema(defaultValue = "연령대")
    AgeType ageType,
    @Schema(defaultValue = "성별")
    GenderType genderType,
    Survey survey
) {
}