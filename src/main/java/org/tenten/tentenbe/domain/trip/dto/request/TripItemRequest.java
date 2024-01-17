package org.tenten.tentenbe.domain.trip.dto.request;

import java.time.LocalDate;

public record TripItemRequest(
    Long tourItemId,
    LocalDate visitDate
) {
}
