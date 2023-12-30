package org.tenten.tentenbe.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CheckResponse(
    @Schema(description = "중복 여부", defaultValue = "true")
    boolean exists
) {
}
