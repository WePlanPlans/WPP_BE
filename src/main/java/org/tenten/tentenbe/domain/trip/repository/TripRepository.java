package org.tenten.tentenbe.domain.trip.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse;
import org.tenten.tentenbe.domain.trip.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
    @Query("SELECT new org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse(" +
        "t.id, " +
        "t.tripName, " +
        "t.startDate, " +
        "t.endDate, " +
        "CAST(COALESCE(COUNT(DISTINCT tmAll.id), 0) AS LONG), " +
        "(CASE WHEN t.startDate > CURRENT_DATE THEN '여행전' " +
        "WHEN t.endDate < CURRENT_DATE THEN '여행완료' ELSE '여행중' END), " +
        "COALESCE(tri.smallThumbnailUrl, 'https://common.hanmi.co.kr/upfile/ces/product/p_2011_tenten_p_400.jpg')," +
        "t.area," +
        "t.subarea) " +
        "FROM Trip t " +
        "LEFT OUTER JOIN TripItem ti ON t.id = ti.trip.id " +
        "LEFT OUTER JOIN TourItem tri ON ti.tourItem.id = tri.id " +
        "LEFT OUTER JOIN TripMember tmAll ON t.id = tmAll.trip.id " +
        "LEFT OUTER JOIN TripMember tm ON t.id = tm.trip.id " +
        "WHERE tm.member.id = :memberId AND t.isDeleted = FALSE " +
        "GROUP BY t.id ORDER BY t.createdTime")
    Page<TripSimpleResponse> findTripsByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
