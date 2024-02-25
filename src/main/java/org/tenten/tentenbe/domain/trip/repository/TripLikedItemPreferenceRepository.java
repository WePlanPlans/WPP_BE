package org.tenten.tentenbe.domain.trip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.trip.model.TripLikedItem;
import org.tenten.tentenbe.domain.trip.model.TripLikedItemPreference;
import org.tenten.tentenbe.domain.trip.model.TripMember;

import java.util.Optional;

public interface TripLikedItemPreferenceRepository extends JpaRepository<TripLikedItemPreference, Long> {

    Optional<TripLikedItemPreference> findByTripMemberAndTripLikedItem(TripMember tripMember, TripLikedItem tripLikedItem);

    long countByTripLikedItemIdAndPrefer(Long tripLikedItem_id, Boolean prefer);
}