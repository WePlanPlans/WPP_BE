package org.tenten.tentenbe.domain.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.trip.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
}
