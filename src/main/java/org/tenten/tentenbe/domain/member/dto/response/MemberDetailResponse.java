package org.tenten.tentenbe.domain.member.dto.response;

import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.global.common.enums.AgeType;
import org.tenten.tentenbe.global.common.enums.GenderType;
import org.tenten.tentenbe.global.common.enums.LoginType;

public record MemberDetailResponse(
    LoginType loginType,
    String nickname,
    String email,
    String profileImageUrl,
    AgeType ageType,
    GenderType genderType,
    String survey
) {
    public static MemberDetailResponse fromEntity(Member member) {
        return new MemberDetailResponse(
            member.getLoginType(),
            member.getNickname(),
            member.getEmail(),
            member.getProfileImageUrl(),
            member.getAgeType(),
            member.getGenderType(),
            member.getSurvey().toString()
        );
    }
}
