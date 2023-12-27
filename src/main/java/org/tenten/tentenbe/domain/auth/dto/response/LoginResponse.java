package org.tenten.tentenbe.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse (

    @Schema(defaultValue = "jwtToken")
    String token
) {
}
