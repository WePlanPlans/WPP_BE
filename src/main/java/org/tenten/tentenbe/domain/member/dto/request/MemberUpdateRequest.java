package org.tenten.tentenbe.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.global.common.enums.AgeType;
import org.tenten.tentenbe.global.common.enums.GenderType;

public record MemberUpdateRequest(
    @Schema(defaultValue = "zkzkzkzk")
    String nickname,
    @Schema(defaultValue = "http://hfstdfg.jpg")
    String profileImageUrl,
    @Schema(defaultValue = "TWENTIES")
    AgeType ageType,
    @Schema(defaultValue = "MALE")
    GenderType genderType
) {
}