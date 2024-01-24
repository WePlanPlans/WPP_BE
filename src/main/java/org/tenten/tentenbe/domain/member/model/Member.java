package org.tenten.tentenbe.domain.member.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.member.dto.request.MemberUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.request.SurveyUpdateRequest;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.trip.model.TripMember;
import org.tenten.tentenbe.global.common.BaseTimeEntity;
import org.tenten.tentenbe.global.common.enums.AgeType;
import org.tenten.tentenbe.global.common.enums.GenderType;
import org.tenten.tentenbe.global.common.enums.LoginType;
import org.tenten.tentenbe.global.common.enums.UserAuthority;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = JOINED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "memberId")
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profileImageUrl;
    @Convert(converter = Survey.SurveyConverter.class)
    @Column(columnDefinition = "JSON")
    private Survey survey;
    @Enumerated(STRING)
    private UserAuthority userAuthority;
    @Enumerated(STRING)
    private LoginType loginType;
    @Enumerated(STRING)
    private AgeType ageType;
    @Enumerated(STRING)
    private GenderType genderType;

    @OneToMany(mappedBy = "member", fetch = LAZY, cascade = REMOVE)
    private final List<TripMember> tripMembers = new ArrayList<>();

    @OneToMany(mappedBy = "creator", fetch = LAZY, cascade = REMOVE)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "creator", fetch = LAZY, cascade = REMOVE)
    private final List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = LAZY, cascade = REMOVE)
    private final List<LikedItem> likedItems = new ArrayList<>();

    public void updateMember(MemberUpdateRequest updateRequest) {
        this.nickname = updateRequest.nickname();
        this.profileImageUrl = updateRequest.profileImageUrl();
        this.ageType = updateRequest.ageType();
        this.genderType = updateRequest.genderType();
    }

    public void updateSurvey(SurveyUpdateRequest surveyUpdateRequest) {
        this.survey = surveyUpdateRequest.survey();
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
