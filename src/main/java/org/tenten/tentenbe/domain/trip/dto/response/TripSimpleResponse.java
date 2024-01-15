package org.tenten.tentenbe.domain.trip.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

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
    Long numberOfPeople,
    @Schema(defaultValue = "여행 전")
    String tripStatus,
    @Schema(defaultValue = "https://~~~~.png")
    String tripThumbnailUrl,
    @Schema(defaultValue = "서울시")
    String area,
    @Schema(defaultValue = "강남구")
    String subArea
) {
}


