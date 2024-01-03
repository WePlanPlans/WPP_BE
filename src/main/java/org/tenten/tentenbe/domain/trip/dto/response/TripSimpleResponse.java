package org.tenten.tentenbe.domain.trip.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.global.common.enums.TripStatus;

import java.time.LocalDate;

public record TripSimpleResponse(
    @Schema(defaultValue = "1")
    Long tripId,
    @Schema(defaultValue = "나의 ~번째 여정")
    String tripName,
    @Schema(defaultValue = "2023-12-27")
    LocalDate startDate,
    @Schema(defaultValue = "2023-12-29")
    LocalDate endDate,
    @Schema(defaultValue = "2")
    Long numberOfTripMembers,
    @Schema(defaultValue = "여행 전")
    TripStatus tripStatus,
    @Schema(defaultValue = "https://~~~~.png")
    String tripThumbnailUrl
) {
}


