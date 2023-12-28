package org.tenten.tentenbe.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ReviewResponse(
    @Schema(defaultValue = "4.5")
    Double ratingAverage,
    @Schema(defaultValue = "10")
    Long reviewTotalCount,
    @Schema(defaultValue = "10")
    Long keywordTotalCount,
    List<ReviewInfo> reviewInfos,
    List<TourKeywordInfo> tourKeywordInfos
) {

}
