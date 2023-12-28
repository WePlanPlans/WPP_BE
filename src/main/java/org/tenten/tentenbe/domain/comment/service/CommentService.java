package org.tenten.tentenbe.domain.comment.service;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.comment.dto.request.CommentCreateRequest;
import org.tenten.tentenbe.domain.comment.dto.request.CommentUpdateRequest;
import org.tenten.tentenbe.domain.comment.dto.response.CommentInfo;
import org.tenten.tentenbe.domain.comment.dto.response.CommentResponse;
import org.tenten.tentenbe.domain.comment.exception.CommentException;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.comment.repository.CommentRepository;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public CommentInfo createComment(CommentCreateRequest commentCreateRequest) {

        Review review = reviewRepository.findById(commentCreateRequest.reviewId()).orElseThrow(
            ()-> new RuntimeException("해당 리뷰가 없습니다.")
        );

        Comment newComment = new Comment(commentCreateRequest.content(), review);

        newComment.addReview(review);
        // newComment.addCreator();
        Comment savedComment = commentRepository.save(newComment);

		return new CommentInfo(
            savedComment.getId(),
            "사용자 이름",
            "사용자 프로필",
            savedComment.getContent(),
            savedComment.getCreatedTime()
        );
    }

    @Transactional
    public CommentInfo updateComment(Long memberId, Long commentId, CommentUpdateRequest commentUpdateRequest) {

        Comment findCommentById = commentRepository.findById(commentId).orElseThrow(
            () -> new CommentException("해당 댓글이 없습니다.", HttpStatus.BAD_REQUEST)
        );

        findCommentById.UpdateComment(commentUpdateRequest.content());
        return new CommentInfo(
            findCommentById.getId(),
            "사용자 이름",
            "사용자 프로필",
            commentUpdateRequest.content(),
            findCommentById.getModifiedTime()
        );
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {

        Comment findCommentById = commentRepository.findById(commentId).orElseThrow(
            () -> new CommentException("해당 댓글이 없습니다.", HttpStatus.BAD_REQUEST)
        );

        findCommentById.removeReview();
        findCommentById.removeCreator();

        commentRepository.delete(findCommentById);
    }
}
