package org.tenten.tentenbe.domain.tour.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.tour.model.TourItem;

public interface TourItemRepository extends JpaRepository<TourItem, Long>, JpaSpecificationExecutor<TourItem> {

    @Query("SELECT NEW org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse(" +
        "ti.id, " +
        "ti.contentTypeId, " +
        "ti.title, " +
        "CAST(COALESCE(AVG(r.rating), 0) AS DOUBLE), " +
        "ti.reviewTotalCount, " +
        "ti.likedTotalCount, " +
        "COALESCE(CASE WHEN (SELECT COALESCE(COUNT(tli.id), 0) FROM LikedItem tli WHERE tli.tourItem.id = ti.id AND tli.member.id = :memberId) = 1 then true end, false), " +
        "ti.smallThumbnailUrl, " +
        "ti.address, " +
        "ti.longitude, " +
        "ti.latitude)" +
        "FROM TourItem ti " +
        "LEFT OUTER JOIN Review r ON ti.id = r.tourItem.id " +
        "LEFT OUTER JOIN LikedItem li ON ti.id = li.tourItem.id " +
        "GROUP BY ti.id " +
        "ORDER BY ti.likedTotalCount DESC, COALESCE(AVG(r.rating), 0) DESC, ti.reviewTotalCount DESC, ti.title ASC")
    Page<TourSimpleResponse> findPopularTourItems(@Param("memberId") Long memberId, Pageable pageable);

    @Query("SELECT NEW org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse(" +
        "ti.id, " +
        "ti.contentTypeId, " +
        "ti.title, " +
        "CAST(COALESCE(AVG(r.rating), 0) AS DOUBLE), " +
        "ti.reviewTotalCount, " +
        "ti.likedTotalCount, " +
        "COALESCE(CASE WHEN (SELECT COALESCE(COUNT(tli.id), 0) FROM LikedItem tli WHERE tli.tourItem.id = ti.id AND tli.member.id = :memberId) = 1 then true end, false), " +
        "ti.smallThumbnailUrl, " +
        "ti.address, " +
        "ti.longitude, " +
        "ti.latitude)" +
        "FROM TourItem ti " +
        "LEFT OUTER JOIN Review r ON ti.id = r.tourItem.id " +
        "LEFT OUTER JOIN LikedItem li ON ti.id = li.tourItem.id " +
        "WHERE ti.areaCode = :areaCode " +
        "GROUP BY ti.id " +
        "ORDER BY ti.likedTotalCount DESC, COALESCE(AVG(r.rating), 0) DESC, ti.reviewTotalCount DESC, ti.title ASC")
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
