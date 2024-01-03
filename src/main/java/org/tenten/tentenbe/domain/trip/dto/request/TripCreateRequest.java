package org.tenten.tentenbe.domain.trip.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Optional;

public record TripCreateRequest(
    @Schema(defaultValue = "나의 N번째 여정")
    Optional<String> tripName,
    @Schema(defaultValue = "2")
    Optional<Long> numberOfPeople,
    @Schema(defaultValue = "2023-12-27")
    Optional<LocalDate> startDate,
    @Schema(defaultValue = "2023-12-28")
    Optional<LocalDate> endDate,
    @Schema(defaultValue = "서울시")
    Optional<String> area,
    @Schema(defaultValue = "강남구")
    Optional<String> subarea
) {
}
