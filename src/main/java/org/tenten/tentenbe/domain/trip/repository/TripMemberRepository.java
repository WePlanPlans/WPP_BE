package org.tenten.tentenbe.domain.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.trip.model.TripMember;

public interface TripMemberRepository extends JpaRepository<TripMember, Long> {

    Long countTripMemberByMember(Member member);
}
