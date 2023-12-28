package org.tenten.tentenbe.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tenten.tentenbe.domain.review.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN FETCH r.reviewKeywords rk JOIN FETCH rk.keyword WHERE r.tourItem.id = :tourItemId")
    List<Review> findReviewByTourItemId(@Param("tourItemId") Long tourItemId);
}
