package org.tenten.tentenbe.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tenten.tentenbe.domain.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c JOIN FETCH c.creator WHERE c.review.id = :reviewId")
    Page<Comment> findCommentsByReviewId(@Param("reviewId") Long reviewId, Pageable pageable);
}
