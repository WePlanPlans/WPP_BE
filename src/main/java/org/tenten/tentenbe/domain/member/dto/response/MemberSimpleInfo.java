package org.tenten.tentenbe.domain.member.dto.response;

public record MemberSimpleInfo(
    Long memberId,
    String nickname,
    String thumbnail
) {
}
