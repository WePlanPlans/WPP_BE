package org.tenten.tentenbe.domain.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.trip.model.TripItem;

public interface TripItemRepository extends JpaRepository<TripItem, Long> {
}
