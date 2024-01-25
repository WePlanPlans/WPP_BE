package org.tenten.tentenbe.domain.comment.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.tenten.tentenbe.common.ServiceTest;
import org.tenten.tentenbe.config.WithMockCustomUser;
import org.tenten.tentenbe.domain.comment.dto.response.CommentInfo;
import org.tenten.tentenbe.domain.comment.exception.CommentException;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.comment.repository.CommentRepository;
import org.tenten.tentenbe.domain.member.exception.MemberException;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.review.exception.ReviewException;
import org.tenten.tentenbe.domain.review.repository.ReviewRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.CommentFixture.*;
import static org.tenten.tentenbe.common.fixture.ReviewFixture.review;


class CommentServiceTest extends ServiceTest {

	@InjectMocks
	private CommentService commentService;
	@Mock
	private CommentRepository commentRepository;
	@Mock
	private ReviewRepository reviewRepository;
	@Mock
	private MemberRepository memberRepository;


	@Test
	@DisplayName("댓글 생성시 리뷰가 없을 경우 NOT_FOUND 에러를 표시한다.")
	public void notFoundReview(){
		given(reviewRepository.findById(anyLong()))
				.willThrow(new ReviewException("해당 아이디로 존재하는 리뷰가 없습니다.", NOT_FOUND));

		ReviewException exception = Assertions.assertThrows(ReviewException.class, () -> commentService.createComment(1L, commentCreateRequest()));
		assertThat(NOT_FOUND).isEqualTo(exception.getErrorCode());
	}

	@Test
	@DisplayName("사용자가 없을시 사용자 NOT_FOUND 에러를 표시한다.")
	public void notFoundUser(){
		given(reviewRepository.findById(anyLong())).willReturn(Optional.ofNullable(review()));
		given(memberRepository.findById(anyLong()))
				.willThrow(new MemberException("주어진 아이디로 존재하는 멤버가 없습니다.", NOT_FOUND));

		MemberException exception = Assertions.assertThrows(MemberException.class, () -> commentService.createComment(1L, commentCreateRequest()));
		assertThat(NOT_FOUND).isEqualTo(exception.getErrorCode());
	}

	@Test
	@DisplayName("댓글 수정시 댓글이 없을 경우 NOT_FOUND 에러를 표시한다.")
	public void notFoundComment(){
		given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(newBasicMember()));
		given(commentRepository.findById(any()))
				.willThrow(new CommentException("해당 댓글이 없습니다.", NOT_FOUND));

		CommentException exception = Assertions.assertThrows(CommentException.class, () -> commentService.updateComment(1L,1L,commentUpdateRequest()));
		assertThat(NOT_FOUND).isEqualTo(exception.getErrorCode());
	}

	@Test
	@DisplayName("댓글 수정시 댓글이 없을 경우 NOT_FOUND 에러를 표시한다.")
	public void noUpdatePermissionComment(){
		given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(newBasicMember()));
		given(commentRepository.findById(any())).willReturn(Optional.of(comment()));

		CommentException exception = Assertions.assertThrows(CommentException.class, () -> commentService.updateComment(1L,1L,commentUpdateRequest()));
		assertThat(FORBIDDEN).isEqualTo(exception.getErrorCode());
	}

	@Test
	@WithMockCustomUser
	@DisplayName("댓글 삭제시 댓글이 없을경우 NOT_FOUND 에러를 표시한다.")
	public void noDeletePermissionComment(){
		given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(newBasicMember()));
		given(commentRepository.findById(any())).willReturn(Optional.of(comment()));

		CommentException exception = Assertions.assertThrows(CommentException.class, () -> commentService.deleteComment(1L,1L));
		assertThat(FORBIDDEN).isEqualTo(exception.getErrorCode());
	}


	@Test
	@WithMockCustomUser
	@DisplayName("댓글 생성 성공")
	public void createCommentTest(){

		Comment savedComment = saveComment(commentCreateRequest().content(), review(), newBasicMember());

		given(reviewRepository.findById(anyLong())).willReturn(Optional.ofNullable(review()));
		given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(newBasicMember()));
		given(commentRepository.save(any())).willReturn(savedComment);

		CommentInfo result = commentService.createComment(newBasicMember().getId(), commentCreateRequest());

		assertThat(result.authorNickname()).isEqualTo("nickNameTest");
		assertThat(result.content()).isEqualTo("success create comment");
		assertThat(result.authorProfileImageUrl()).isEqualTo("naver.com");
		assertThat(result.isAuthor()).isEqualTo(true);
	}

	@Test
	@WithMockCustomUser
	@DisplayName("댓글 업데이트 성공")
	public void updateCommentTest(){
		Comment savedComment = saveComment(commentCreateRequest().content(), review(), newBasicMember());

		given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(newBasicMember()));
		given(commentRepository.findById(any())).willReturn(Optional.of(savedComment));

		CommentInfo result = commentService.updateComment(newBasicMember().getId(), comment().getId(),commentUpdateRequest());

		assertThat(result.authorNickname()).isEqualTo("nickNameTest");
		assertThat(result.content()).isEqualTo("update comment");
		assertThat(result.authorProfileImageUrl()).isEqualTo("naver.com");
		assertThat(result.isAuthor()).isEqualTo(true);
	}

	@Test
	@WithMockCustomUser
	@DisplayName("댓글 삭제 성공")
	public void deleteCommentTest(){
		Comment savedComment = saveComment(commentCreateRequest().content(), review(), newBasicMember());

		given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(newBasicMember()));
		given(commentRepository.findById(any())).willReturn(Optional.of(savedComment));

		assertDoesNotThrow(() -> commentService.deleteComment(newBasicMember().getId(), comment().getId()));
	}
}