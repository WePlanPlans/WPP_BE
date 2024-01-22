package org.tenten.tentenbe.domain.trip.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record TripSimpleResponse(
    @Schema(defaultValue = "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b")
    String tripId,
    @Schema(defaultValue = "나의 ~번째 여정")
    String tripName,
    @Schema(defaultValue = "2023-12-27")
    LocalDate startDate,
    @Schema(defaultValue = "2023-12-29")
    LocalDate endDate,
    @Schema(defaultValue = "2")
    Long numberOfPeople,
    @Schema(defaultValue = "2")
    Long numberOfTripMember,
    @Schema(defaultValue = "여행 전")
    String tripStatus,
    @Schema(defaultValue = "https://~~~~.png")
    String tripThumbnailUrl
) {
}


