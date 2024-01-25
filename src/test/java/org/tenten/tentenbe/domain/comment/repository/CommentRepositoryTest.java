package org.tenten.tentenbe.domain.comment.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.tenten.tentenbe.common.ControllerTest;
import org.tenten.tentenbe.common.RepositoryTest;
import org.tenten.tentenbe.config.WithMockCustomUser;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.review.dto.request.ReviewCreateRequest;
import org.tenten.tentenbe.domain.review.dto.request.ReviewUpdateRequest;
import org.tenten.tentenbe.domain.review.dto.response.TourKeywordInfo;
import org.tenten.tentenbe.domain.review.model.Keyword;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.review.service.ReviewService;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.CommentFixture.commentResponse;
import static org.tenten.tentenbe.common.fixture.CommentFixture.saveComment;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.common.fixture.ReviewFixture.*;

public class CommentRepositoryTest extends RepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Test
    @DisplayName("코멘트 저장이 가능하다.")
    public void saveCommentSuccess() {
        //given
        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));
        String content = "can create comment";

        //when
        Comment comment = saveComment(content,savedReview,savedMember);
        Comment savedComment = commentRepository.save(comment);
        //then
        assertThat(content).isEqualTo(savedComment.getContent());
        assertThat(comment).isEqualTo(savedComment);
    }

    @Test
    @DisplayName("코멘트 조회가 가능하다.")
    public void findCommentSuccess() {
        //given
        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));
        String content = "can create comment";

        Comment comment = saveComment(content,savedReview,savedMember);
        Comment comment2 = saveComment(content,savedReview,savedMember);
        Comment comment3 = saveComment(content,savedReview,savedMember);

        commentRepository.saveAll(List.of(comment,comment2,comment3));
        //when
        List<Comment> findAllByComment = commentRepository.findAll();

        //then
        assertThat(findAllByComment.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("코멘트 저장 후 리뷰 조회가 가능하다.")
    public void reviewCanFindComment() {
        //given
        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));
        String content = "can create comment";

        Comment comment = saveComment(content,savedReview,savedMember);

        Comment savedComment = commentRepository.save(comment);
        //when
        Optional<Comment> findAllByComment = commentRepository.findById(savedComment.getId());

        //then
        assertNotNull(findAllByComment);
        assertThat(findAllByComment.get().getReview().getContent()).isEqualTo(savedReview.getContent());
        assertThat(findAllByComment.get().getReview()).isEqualTo(savedReview);
    }

    @Test
    @DisplayName("코멘트 수정이 가능하다.")
    public void updateCommentSuccess() {
        //given
        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));
        String content = "can create comment";
        String updateContent = "comment is updated";

        Comment comment = saveComment(content,savedReview,savedMember);
        Comment savedComment = commentRepository.save(comment);
        savedComment.UpdateComment(updateContent);
        //when
        Optional<Comment> findByComment = commentRepository.findById(savedComment.getId());

        //then
        assertNotNull(findByComment);
        assertThat(findByComment.get().getContent()).isEqualTo(updateContent);
    }

    @Test
    @DisplayName("코멘트 삭제가 가능하다.")
    public void deleteCommentSuccess() {
        //given
        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));
        String content = "can create comment";

        Comment comment = saveComment(content,savedReview,savedMember);

        //when then
        Comment savedComment = commentRepository.save(comment);
        assertThat(commentRepository.findAll().size()).isEqualTo(1);

        commentRepository.deleteById(savedComment.getId());
        assertThat(commentRepository.findAll().size()).isEqualTo(0);
    }
}
