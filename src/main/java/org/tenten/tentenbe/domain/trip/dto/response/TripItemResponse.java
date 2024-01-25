package org.tenten.tentenbe.domain.trip.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record TripItemResponse(
    @Schema(defaultValue = "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b")
    String tripId,
    @Schema(defaultValue = "1")
    Long tripItemId,
    @Schema(defaultValue = "1")
    Long tourItemId,
    @Schema(defaultValue = "2024-01-04")
    LocalDate visitDate
) {
}
