package org.tenten.tentenbe.domain.member.dto.response;

import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.model.Survey;
import org.tenten.tentenbe.global.common.enums.AgeType;
import org.tenten.tentenbe.global.common.enums.GenderType;

public record MemberUpdateResponse(
    String nickname,
    String profileImageUrl,
    AgeType ageType,
    GenderType genderType,
    Survey survey
) {
    public static MemberUpdateResponse fromEntity(Member member) {
        return new MemberUpdateResponse(
            member.getNickname(),
            member.getProfileImageUrl(),
            member.getAgeType(),
            member.getGenderType(),
            member.getSurvey()
        );
    }
}