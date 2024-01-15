package org.tenten.tentenbe.domain.trip.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tenten.tentenbe.domain.trip.dto.response.TripLikedSimpleResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse;
import org.tenten.tentenbe.domain.trip.model.Trip;
import org.tenten.tentenbe.global.common.enums.Category;

public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("SELECT new org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse(" +
        "t.id, " +
        "t.tripName, " +
        "t.startDate, " +
        "t.endDate, " +
        "t.numberOfPeople, " +
        "(CASE WHEN t.startDate > CURRENT_DATE THEN '여행 전' " +
        "WHEN t.endDate < CURRENT_DATE THEN '여행 후' ELSE '여행 중' END), " +
        "COALESCE(tri.smallThumbnailUrl, 'https://common.hanmi.co.kr/upfile/ces/product/p_2011_tenten_p_400.jpg')) " +
        "FROM Trip t " +
        "LEFT OUTER JOIN TripItem ti ON t.id = ti.trip.id " +
        "LEFT OUTER JOIN TourItem tri ON ti.tourItem.id = tri.id " +
        "LEFT OUTER JOIN TripMember tm ON t.id = tm.trip.id " +
        "WHERE tm.member.id = :memberId AND t.isDeleted = FALSE " +
        "GROUP BY t.id ORDER BY t.createdTime")
    Page<TripSimpleResponse> findTripsByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
