package org.tenten.tentenbe.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.comment.dto.request.CommentCreateRequest;
import org.tenten.tentenbe.domain.comment.dto.request.CommentUpdateRequest;
import org.tenten.tentenbe.domain.comment.dto.response.CommentInfo;
import org.tenten.tentenbe.domain.comment.exception.CommentException;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.comment.repository.CommentRepository;
import org.tenten.tentenbe.domain.member.exception.MemberException;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.review.exception.ReviewException;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.review.repository.ReviewRepository;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommentInfo createComment(Long currentMemberId, CommentCreateRequest commentCreateRequest) {

        Review review = reviewRepository.findById(commentCreateRequest.reviewId())
                .orElseThrow(() -> new ReviewException("해당 아이디로 존재하는 리뷰가 없습니다. reviewId : " + commentCreateRequest.reviewId(), NOT_FOUND));
        Member member = memberRepository.findById(currentMemberId).orElseThrow(() -> new MemberException("주어진 아이디로 존재하는 멤버가 없습니다.", NOT_FOUND));

        Comment newComment = new Comment(commentCreateRequest.content(), review,member);
        Comment savedComment = commentRepository.save(newComment);

		return new CommentInfo(
            savedComment.getId(),
            member.getNickname(),
            member.getProfileImageUrl(),
            savedComment.getContent(),
            savedComment.getCreatedTime(),
                true
        );
    }

    @Transactional
    public CommentInfo updateComment(Long memberId, Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException("주어진 아이디로 존재하는 멤버가 없습니다.", NOT_FOUND));

        Comment findCommentById = commentRepository.findById(commentId).orElseThrow(
            () -> new CommentException("해당 댓글이 없습니다.", NOT_FOUND)
        );

        commentEditPermission(member,findCommentById);

        findCommentById.UpdateComment(commentUpdateRequest.content());
        return new CommentInfo(
            findCommentById.getId(),
            member.getNickname(),
            member.getProfileImageUrl(),
            commentUpdateRequest.content(),
            findCommentById.getModifiedTime(),
            true
        );
    }

    @Transactional
    public void deleteComment(Long memberId, Long commentId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException("주어진 아이디로 존재하는 멤버가 없습니다.", NOT_FOUND));
        Comment findCommentById = commentRepository.findById(commentId).orElseThrow(
            () -> new CommentException("해당 댓글이 없습니다.", NOT_FOUND)
        );

        commentEditPermission(member,findCommentById);
        commentRepository.delete(findCommentById);
    }

    private void commentEditPermission(Member member, Comment comment){
        if(member.getId() != comment.getCreator().getId()){
            throw new CommentException("댓글 수정 권한이 없습니다.",HttpStatus.FORBIDDEN);
        }
    }
}
