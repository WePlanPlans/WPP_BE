package org.tenten.tentenbe.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.domain.member.model.Survey;

public record MemberUpdateRequest(
    @Schema(defaultValue = "닉네임")
    String nickname,
    Survey survey,
    @Schema(defaultValue = "프로필 이미지")
    String profileImageUrl,
    @Schema(defaultValue = "as@#SD23/&DFd%fs@a1")
    String password
) {
}
