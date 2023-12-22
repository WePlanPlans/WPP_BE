package org.tenten.tentenbe.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.review.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
