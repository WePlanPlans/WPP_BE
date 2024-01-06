package org.tenten.tentenbe.domain.tour.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import java.util.List;

public interface TourItemRepository extends JpaRepository<TourItem, Long>, JpaSpecificationExecutor<TourItem> {
    List<TourItem> findByAreaCode(Long areaCode);

    @Query("SELECT NEW org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse(" +
        "ti.id, " +
        "ti.contentTypeId, " +
        "ti.title, " +
        "CAST(COALESCE(AVG(r.rating), 0) AS DOUBLE), " +
        "CAST(COALESCE(COUNT(r.id), 0) AS LONG), " +
        "CAST(COALESCE(COUNT(li.id), 0) AS LONG), " +
        "COALESCE((li.member.id = :memberId), false), " +
        "ti.smallThumbnailUrl, " +
        "ti.address, " +
        "ti.longitude, " +
        "ti.latitude)" +
        "FROM TourItem ti " +
        "LEFT OUTER JOIN Review r ON ti.id = r.tourItem.id " +
        "LEFT OUTER JOIN LikedItem li ON ti.id = li.tourItem.id " +
        "GROUP BY ti.id " +
        "ORDER BY COALESCE(COUNT(li.id), 0) DESC, COALESCE(AVG(r.rating), 0) DESC, COUNT(r.id) DESC, ti.title ASC")
    Page<TourSimpleResponse> findPopularTourItems(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT NEW org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse(" +
        "ti.id, " +
        "ti.contentTypeId, " +
        "ti.title, " +
        "CAST(COALESCE(AVG(r.rating), 0) AS DOUBLE), " +
        "CAST(COALESCE(COUNT(DISTINCT r.id), 0) AS LONG), " +
        "CAST(COALESCE(COUNT(DISTINCT li.id), 0) AS LONG), " +
        "COALESCE((li.member.id = :memberId), false), " +
        "ti.smallThumbnailUrl, " +
        "ti.address, " +
        "ti.longitude, " +
        "ti.latitude)" +
        "FROM TourItem ti " +
        "LEFT OUTER JOIN Review r ON ti.id = r.tourItem.id " +
        "LEFT OUTER JOIN LikedItem li ON ti.id = li.tourItem.id " +
        "WHERE ti.areaCode = :areaCode " +
        "GROUP BY ti.id " +
        "ORDER BY COALESCE(COUNT(li.id), 0) DESC, COALESCE(AVG(r.rating), 0) DESC, COUNT(r.id) DESC, ti.title ASC")
    Page<TourSimpleResponse> findPopularTourItems(
        @Param("areaCode") Long areaCode,
        @Param("memberId") Long memberId,
        Pageable pageable
    );

    @Query("SELECT t FROM TourItem t WHERE " +
        "(t.areaCode = :region OR :region IS NULL) AND " +
        "(t.contentTypeId = :category OR :category IS NULL) AND " +
        "(t.title LIKE %:searchWord% OR t.address LIKE %:searchWord% OR t.detailedAddress LIKE %:searchWord%)")
    Page<TourItem> searchByRegionAndCategoryAndSearchWord(
        @Param("region") Long region,
        @Param("category") Long category,
        @Param("searchWord") String searchWord,
        Pageable pageable
    );
}
