package org.tenten.tentenbe.domain.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tenten.tentenbe.domain.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findCommentsByReviewId(Long reviewId, Pageable pageable);
}
