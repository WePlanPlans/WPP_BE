package org.tenten.tentenbe.domain.tour.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.global.common.enums.KeywordType;

public record TourKeywordSimpleInfo(
    @Schema(defaultValue = "1")
    Long id,
    @Schema(defaultValue = "사장님이 친절해요")
    String keywordContent,
    @Schema(defaultValue = "39")
    KeywordType type
) {
}
