package org.tenten.tentenbe.domain.trip.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.global.common.BaseTimeEntity;
import org.tenten.tentenbe.global.common.enums.TripAuthority;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = JOINED)
public class TripMember extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tripMemberId")
    private Long id;
    @Enumerated(STRING)
    private TripAuthority tripAuthority;

    @ManyToOne
    @JoinColumn(name = "tripId")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
}
