package org.tenten.tentenbe.domain.liked.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import java.util.Optional;

public interface LikedItemRepository extends JpaRepository<LikedItem, Long> {
    Optional<LikedItem> findByMemberAndTourItem(Member member, TourItem tourItem);

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
        "ti.latitude) " +
        "FROM TourItem ti " +
        "LEFT OUTER JOIN Review r ON ti.id = r.tourItem.id " +
        "LEFT OUTER JOIN LikedItem li ON ti.id = li.tourItem.id " +
        "WHERE li.member.id = :memberId " +
        "AND (ti.contentTypeId = :category OR :category IS NULL) " +
        "GROUP BY ti.id")
    Page<TourSimpleResponse> findByMemberAndCategory(
        @Param("memberId") Long memberId,
        @Param("category") Long category,
        Pageable pageable
    );

    boolean existsByMemberIdAndTourItemId(Long memberId, Long tourItemId);
}
