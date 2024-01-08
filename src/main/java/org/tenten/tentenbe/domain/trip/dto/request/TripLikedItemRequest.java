package org.tenten.tentenbe.domain.trip.dto.request;

import java.util.List;

public record TripLikedItemRequest(
    List<Long> tourItemIds
) {
}
