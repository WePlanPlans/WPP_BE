package org.tenten.tentenbe.global.security.oauth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.tenten.tentenbe.domain.auth.dto.response.MemberDto;
import org.tenten.tentenbe.domain.token.dto.TokenDTO;

@Builder
public record OAuthLoginResponse (
    @Schema(name = "멤버 정보")
    MemberDto memberDto,

    @Schema(name = "토큰 정보")
    TokenDTO.TokenIssueDTO tokenInfo,

    @Schema(name = "리다이렉트 url")
    String redirectUrl
) {
}
