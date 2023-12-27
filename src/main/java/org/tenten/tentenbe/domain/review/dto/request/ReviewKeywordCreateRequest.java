package org.tenten.tentenbe.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReviewKeywordCreateRequest(
    @Schema(defaultValue = "1")
    Long keywordId,
    @Schema(defaultValue = "좋아요")
    String content
) {
}
