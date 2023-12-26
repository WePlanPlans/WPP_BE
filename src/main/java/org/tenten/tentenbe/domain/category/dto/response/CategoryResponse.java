package org.tenten.tentenbe.domain.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponse(
        @Schema(defaultValue = "39")
        Long code,
        @Schema(defaultValue = "식당")
        String name
) {
}

