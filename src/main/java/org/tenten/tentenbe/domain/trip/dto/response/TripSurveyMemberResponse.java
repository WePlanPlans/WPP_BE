package org.tenten.tentenbe.domain.trip.dto.response;

import org.tenten.tentenbe.domain.member.dto.response.MemberSimpleInfo;

import java.util.List;

public record TripSurveyMemberResponse(
    Long tripSurveyMemberCount,
    List<MemberSimpleInfo> tripSurveySetMemberInfos,
    List<MemberSimpleInfo> nonTripSurveySetMemberInfos
) {
}
