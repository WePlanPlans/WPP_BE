package org.tenten.tentenbe.domain.trip.dto.response;

import java.util.List;

public record TripMembersResponse(
    List<TripMemberSimpleInfo> tripMemberSimpleInfos
) {
    public record TripMemberSimpleInfo(
        String nickname,
        String profileImageUrl
    ) {
    }
}
