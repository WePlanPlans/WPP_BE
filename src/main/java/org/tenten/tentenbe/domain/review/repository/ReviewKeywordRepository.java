package org.tenten.tentenbe.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.review.model.ReviewKeyword;

public interface ReviewKeywordRepository extends JpaRepository<ReviewKeyword, Long> {
    void deleteByReviewId(Long reviewId);
}
