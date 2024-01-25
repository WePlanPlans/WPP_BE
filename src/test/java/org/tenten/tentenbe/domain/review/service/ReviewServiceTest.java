package org.tenten.tentenbe.domain.review.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.tenten.tentenbe.common.ServiceTest;
import org.tenten.tentenbe.domain.comment.dto.response.CommentResponse;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.comment.repository.CommentRepository;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.review.dto.response.KeywordResponse;
import org.tenten.tentenbe.domain.review.dto.response.ReviewInfo;
import org.tenten.tentenbe.domain.review.dto.response.ReviewResponse;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.review.repository.KeywordRepository;
import org.tenten.tentenbe.domain.review.repository.ReviewKeywordRepository;
import org.tenten.tentenbe.domain.review.repository.ReviewRepository;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.CommentFixture.commentCreateRequest;
import static org.tenten.tentenbe.common.fixture.CommentFixture.saveComment;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.common.fixture.ReviewFixture.*;

public class ReviewServiceTest extends ServiceTest {

    @InjectMocks
    private ReviewService reviewService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TourItemRepository tourItemRepository;
    @Mock
    private KeywordRepository keywordRepository;
    @Mock
    private ReviewKeywordRepository reviewKeywordRepository;
    @Mock
    private CommentRepository commentRepository;

    @Test
    @DisplayName("여행 리뷰 조회가 가능하다.")
    public void getTourReviewSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        given(tourItemRepository.findById(any())).willReturn(Optional.of(tourItem()));
        given(reviewRepository.findReviewByTourItemId(any(),any())).willReturn(new PageImpl<>(List.of(saveReview(tourItem(),newBasicMember())), pageable, tourItem().getReviewTotalCount()));
        given(keywordRepository.findKeywordInfoByTourItemIdAndKeywordType(any())).willReturn(List.of(tourKeywordInfo()));

        ReviewResponse result = reviewService.getTourReviews(tourItem().getReviewTotalCount(), pageable, newBasicMember().getId());

        assertNotNull(result);
        assertThat(result.reviewInfos().getContent().get(0).reviewId()).isEqualTo(1L);
        assertThat(result.reviewInfos().getContent().get(0).authorNickname()).isEqualTo("nickNameTest");
        assertThat(result.reviewInfos().getContent().get(0).authorProfileImageUrl()).isEqualTo("naver.com");
        assertThat(result.reviewInfos().getContent().get(0).rating()).isEqualTo(20.5);
        assertThat(result.reviewInfos().getContent().get(0).isAuthor()).isEqualTo(true);
        assertThat(result.reviewTotalCount()).isEqualTo(10);
    }

    @Test
    @DisplayName("리뷰를 작성 할 수 있다.")
    public void createReviewSuccess() {

        given(memberRepository.findById(any())).willReturn(Optional.of(newBasicMember()));
        given(tourItemRepository.findById(any())).willReturn(Optional.of(tourItem()));
        given(reviewRepository.save(any())).willReturn(review());
        given(keywordRepository.findById(any())).willReturn(Optional.of(keyword()));

        ReviewInfo result = reviewService.createReview(newBasicMember().getId(), reviewCreateRequest());

        assertNotNull(result);
        assertThat(result.authorNickname()).isEqualTo("nickNameTest");
        assertThat(result.content()).isEqualTo("review content test");
        assertThat(result.authorProfileImageUrl()).isEqualTo("naver.com");
        assertThat(result.rating()).isEqualTo(2.5);
        assertThat(result.isAuthor()).isEqualTo(true);
    }

    @Test
    @DisplayName("리뷰를 업데이트 할 수 있다.")
    public void updateReviewSuccess() {


        given(memberRepository.findById(any())).willReturn(Optional.of(newBasicMember()));
        given(reviewRepository.findById(any())).willReturn(Optional.of(review()));
        given(keywordRepository.findById(any())).willReturn(Optional.of(keyword()));
        assertDoesNotThrow(()->reviewKeywordRepository.deleteByReviewId(any()));
        given(reviewKeywordRepository.saveAll(any())).willReturn(List.of(reviewKeyword()));

        ReviewInfo result = reviewService.updateReview(newBasicMember().getId(), review().getId(), reviewUpdateRequest());

        // Assertions
        assertNotNull(result);
        assertThat(result.authorNickname()).isEqualTo("nickNameTest");
        assertThat(result.content()).isEqualTo("Review Update Content");
        assertThat(result.authorProfileImageUrl()).isEqualTo("naver.com");
        assertThat(result.rating()).isEqualTo(42.3);
        assertThat(result.isAuthor()).isEqualTo(true);
    }

    @Test
    @DisplayName("리뷰를 삭제할 수 있다.")
    public void deleteReviewSuccess() {

        Review review = review();

        given(memberRepository.findById(any())).willReturn((Optional.of(review.getCreator())));
        given(reviewRepository.findById(any())).willReturn((Optional.of(review)));

        assertDoesNotThrow(() -> reviewService.deleteReview(review().getCreator().getId(), review.getId()));

    }

    @Test
    @DisplayName("리뷰에서 댓글 조회를 할 수 있다.")
    public void getReviewComment(){

        Comment savedComment = saveComment(commentCreateRequest().content(), review(), newBasicMember());
        Pageable pageable = PageRequest.of(0, 10);
        given(commentRepository.findCommentsByReviewId(any(),any())).willReturn(new PageImpl<>(List.of(savedComment), pageable, 10));

        CommentResponse result = reviewService.getReviewComments(review().getId(), pageable, newBasicMember().getId());

        assertNotNull(result);
        assertThat(result.comments().getContent().get(0).authorNickname()).isEqualTo("nickNameTest");
        assertThat(result.comments().getContent().get(0).content()).isEqualTo("success create comment");
        assertThat(result.comments().getContent().get(0).authorProfileImageUrl()).isEqualTo("naver.com");
    }

    @Test
    @DisplayName("리뷰에서 키워드를 조회할 수 있다.")
    public void getKeyWords(){

        KeywordResponse keywordResponse = keywordResponse();
        given(keywordRepository.findAll()).willReturn(List.of(keyword()));
        KeywordResponse result = reviewService.getKeywords(null);
        assertThat(result.keywords()).isEqualTo(keywordResponse.keywords());
    }
}
