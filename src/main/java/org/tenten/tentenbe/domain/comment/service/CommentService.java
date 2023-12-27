package org.tenten.tentenbe.domain.comment.service;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.comment.dto.request.CommentCreateRequest;
import org.tenten.tentenbe.domain.comment.dto.request.CommentUpdateRequest;
import org.tenten.tentenbe.domain.comment.dto.response.CommentResponse;
import org.tenten.tentenbe.domain.comment.exception.CommentException;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.comment.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    @Transactional
    public CommentResponse createComment(CommentCreateRequest commentCreateRequest) {

        Comment newComment = new Comment(commentCreateRequest.content());

        newComment.addCreator(newComment.getCreator());
        newComment.addReview(newComment.getReview());

        commentRepository.save(newComment);

        return new CommentResponse(newComment.getContent(), newComment.getCreator());
    }
    @Transactional
    public CommentResponse updateComment(Long memberId, Long commentId, CommentUpdateRequest commentUpdateRequest) {

        Comment findCommentById = commentRepository.findById(commentId).orElseThrow(
            () -> new CommentException("해당 댓글이 없습니다.", HttpStatus.BAD_REQUEST)
        );

        findCommentById.UpdateComment(commentUpdateRequest.content());
        return new CommentResponse(commentUpdateRequest.content(), null);
    }
    @Transactional
    public void deleteComment(Long memebrId, Long commentId) {

        Comment findCommentById = commentRepository.findById(commentId).orElseThrow(
            () -> new CommentException("해당 댓글이 없습니다.", HttpStatus.BAD_REQUEST)
        );

        findCommentById.removeCreator();
        findCommentById.removeReview();

        commentRepository.delete(findCommentById);
    }
}
