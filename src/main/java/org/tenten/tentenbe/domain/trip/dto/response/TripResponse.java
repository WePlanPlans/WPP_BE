package org.tenten.tentenbe.domain.trip.dto.response;

import org.springframework.data.domain.Page;

public record TripResponse(Page<TripSimpleResponse> trips) {
}
