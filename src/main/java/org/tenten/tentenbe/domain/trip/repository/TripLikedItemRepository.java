package org.tenten.tentenbe.domain.trip.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.domain.trip.dto.response.TripLikedSimpleResponse;
import org.tenten.tentenbe.domain.trip.model.Trip;
import org.tenten.tentenbe.domain.trip.model.TripLikedItem;

import java.util.Optional;

public interface TripLikedItemRepository extends JpaRepository<TripLikedItem, Long> {
    @Query("SELECT new org.tenten.tentenbe.domain.trip.dto.response.TripLikedSimpleResponse(" +
        "tli.id, " +
        "ti.id, " +
        "ti.contentTypeId, " +
        "(SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.tourItem.id = ti.id), " +
        "ti.reviewTotalCount, " +
        "COALESCE((CASE WHEN :memberId IS NOT NULL THEN (CASE WHEN (SELECT COALESCE(COUNT(tli.id), 0) FROM TripLikedItemPreference tp " +
        "WHERE tp.tripLikedItem.id = tli.id AND tp.tripMember.member.id = :memberId) = 1 THEN TRUE ELSE FALSE END) ELSE FALSE END), FALSE), " +
        "(SELECT COUNT(tp) FROM TripLikedItemPreference tp WHERE tp.tripLikedItem.id = tli.id AND tp.prefer = TRUE), " +
        "(SELECT COUNT(tp) FROM TripLikedItemPreference tp WHERE tp.tripLikedItem.id = tli.id AND tp.notPrefer = TRUE)) " +
        "FROM TripLikedItem tli " +
        "LEFT OUTER JOIN TourItem ti ON ti.id = tli.tourItem.id " +
        "LEFT OUTER JOIN Trip t ON t.id = tli.trip.id " +
        "WHERE tli.trip.id = :tripId AND (ti.contentTypeId = :category OR :category IS NULL) " +
        "GROUP BY tli.id ")
    Page<TripLikedSimpleResponse> findTripLikedItemsById(
        @Param("memberId") Long memberId,
        @Param("tripId") Long tripId,
        @Param("category") Long category,
        Pageable pageable
    );

    Optional<TripLikedItem> findByTripAndTourItem(Trip trip, TourItem tourItem);
}