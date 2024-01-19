package org.tenten.tentenbe.domain.trip.dto.response;

import org.tenten.tentenbe.global.common.enums.TripAuthority;

public record TripAuthorityResponse(
    Long memberId,
    TripAuthority tripAuthority
) {
}
