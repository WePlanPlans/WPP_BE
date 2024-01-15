package org.tenten.tentenbe.domain.trip.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TripSurveyResponse(
    @Schema(defaultValue = "10")
    Long planningTotalCount,
    @Schema(defaultValue = "6")
    Long planningCount,
    @Schema(defaultValue = "10")
    Long activeHoursTotalCount,
    @Schema(defaultValue = "6")
    Long activeHoursCount,
    @Schema(defaultValue = "10")
    Long accommodationTotalCount,
    @Schema(defaultValue = "6")
    Long accommodationCount,
    @Schema(defaultValue = "10")
    Long foodTotalCount,
    @Schema(defaultValue = "6")
    Long foodCount,
    @Schema(defaultValue = "10")
    Long tripStyleTotalCount,
    @Schema(defaultValue = "6")
    Long tripStyleCount,
    @Schema(defaultValue = "5")
    Long tripSurveyMemberCount
) {
}
