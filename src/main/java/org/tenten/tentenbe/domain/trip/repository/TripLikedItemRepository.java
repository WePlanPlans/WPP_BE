package org.tenten.tentenbe.domain.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.trip.model.TripLikedItem;

public interface TripLikedItemRepository extends JpaRepository<TripLikedItem, Long> {

}
