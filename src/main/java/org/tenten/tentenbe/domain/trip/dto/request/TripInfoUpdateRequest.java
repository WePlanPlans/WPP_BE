package org.tenten.tentenbe.domain.trip.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record TripInfoUpdateRequest(
    @Schema(defaultValue = "나의 N번째 여정")
    String tripName,
    @Schema(defaultValue = "2")
    Long numberOfPeople,
    @Schema(defaultValue = "2023-12-27")
    LocalDate startDate,
    @Schema(defaultValue = "2023-12-28")
    LocalDate endDate
) {
}
