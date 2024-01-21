package org.tenten.tentenbe.domain.trip.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TripCreateResponse(
    @Schema(defaultValue = "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b")
    String tripId
) {
}
