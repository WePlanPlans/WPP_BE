package org.tenten.tentenbe.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.tenten.tentenbe.domain.token.dto.TokenDTO.TokenIssueDTO;

@Builder
public record LoginResponse (
    @Schema(name = "멤버 정보")
    MemberDto memberDto,

    @Schema(name = "토큰 정보")
    TokenIssueDTO tokenInfo
) {
}
