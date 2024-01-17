package org.tenten.tentenbe.domain.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.trip.model.Trip;
import org.tenten.tentenbe.domain.trip.model.TripMember;

import java.util.List;
import java.util.Optional;

public interface TripMemberRepository extends JpaRepository<TripMember, Long> {

    Long countTripMemberByMember(Member member);

    Optional<TripMember> findByMemberAndTrip(Member member, Trip trip);

    List<TripMember> findByTrip(Trip trip);
}
