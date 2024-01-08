package org.tenten.tentenbe.domain.auth.dto.response;

import lombok.Builder;
import org.tenten.tentenbe.domain.token.dto.TokenDTO.TokenIssueDTO;

@Builder
public record SignUpResponse(
    Long memberId,
    String email,
    String nickName,
    TokenIssueDTO tokenInfo
) {
}
