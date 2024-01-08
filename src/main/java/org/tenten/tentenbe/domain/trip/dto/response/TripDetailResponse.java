package org.tenten.tentenbe.domain.trip.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record TripDetailResponse(
    @Schema(defaultValue = "여정 이름")
    String tripName,
    @Schema(defaultValue = "2024-01-08")
    LocalDate startDate,
    @Schema(defaultValue = "2024-01-10")
    LocalDate endDate
) {
}
