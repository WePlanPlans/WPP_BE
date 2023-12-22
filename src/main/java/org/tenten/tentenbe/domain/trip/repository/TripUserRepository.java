package org.tenten.tentenbe.domain.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.trip.model.TripMember;

public interface TripUserRepository extends JpaRepository<TripMember, Long> {
}
