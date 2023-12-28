package org.tenten.tentenbe.domain.comment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.comment.dto.request.CommentCreateRequest;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.comment.repository.CommentRepository;
import org.tenten.tentenbe.domain.review.model.Review;

@Transactional
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

	@InjectMocks
	private CommentService commentService;

	@Mock
	CommentRepository commentRepository;

	@Test
	@DisplayName("댓글 생성 성공")
	public void createCommentTest(){

		Review review = new Review(1L, 10L, "좋은 여행", null, null);

		CommentCreateRequest commentCreateRequest = new CommentCreateRequest(
			"여행 즐거웠습니다. ",
			review.getId()
		);

	}


}