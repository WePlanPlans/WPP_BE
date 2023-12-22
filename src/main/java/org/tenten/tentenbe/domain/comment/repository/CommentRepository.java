package org.tenten.tentenbe.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
