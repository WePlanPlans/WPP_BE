package org.tenten.tentenbe.domain.trip.dto.response;

import java.time.LocalDate;

public record TripItemResponse(
    Long tripId,
	Long tripItemId,
	Long tourItemId,
	LocalDate visitDate
) {
}
