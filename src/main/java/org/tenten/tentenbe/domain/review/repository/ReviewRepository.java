package org.tenten.tentenbe.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tenten.tentenbe.domain.review.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN FETCH r.reviewKeywords rk JOIN FETCH rk.keyword WHERE r.tourItem.id = :tourItemId")
    Page<Review> findReviewByTourItemId(@Param("tourItemId") Long tourItemId, Pageable pageable);

    Page<Review> findReviewByCreatorId(Long memberId, Pageable pageable);
}
