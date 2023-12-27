package org.tenten.tentenbe.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ReviewUpdateRequest(
    @Schema(defaultValue = "3")
    Long rating,
    List<ReviewKeywordCreateRequest> keywords,
    @Schema(defaultValue = "너무 맘에 드는 여행지였습니다.")
    String content
) {
}
