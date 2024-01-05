package org.tenten.tentenbe.domain.trip.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TripLikedSimpleResponse(
    @Schema(defaultValue = "1")
    Long tripLikedItemId,
    @Schema(defaultValue = "5")
    Long tourItemId,
    @Schema(defaultValue = "관광지")
    String categoryName,
    @Schema(defaultValue = "4.3")
    Double ratingAverage,
    @Schema(defaultValue = "100")
    Long reviewCount,
    @Schema(defaultValue = "false")
    Boolean preferred,
    @Schema(defaultValue = "4")
    Long preferTotalCount,
    @Schema(defaultValue = "2")
    Long notPreferTotalCount
) {

}
