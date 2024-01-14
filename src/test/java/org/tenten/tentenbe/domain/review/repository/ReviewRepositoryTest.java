package org.tenten.tentenbe.domain.review.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.tenten.tentenbe.common.RepositoryTest;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.comment.repository.CommentRepository;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.review.dto.response.TourKeywordInfo;
import org.tenten.tentenbe.domain.review.model.Keyword;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.CommentFixture.saveComment;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.common.fixture.ReviewFixture.*;

public class ReviewRepositoryTest extends RepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Test
    @DisplayName("리뷰 키워드 정보를 조회 할 수 있다. ")
    public void getTourReviewsSuccess() {
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        Member savedMember = memberRepository.save(newBasicMember());
        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));
        Keyword savedKeyword = keywordRepository.save(keyword());
        reviewKeywordRepository.save(saveReviewKeyword(savedReview,savedKeyword));

        List<TourKeywordInfo> result = keywordRepository.findKeywordInfoByTourItemIdAndKeywordType(savedTourItem.getId());

        assertNotNull(result);
        assertThat(result.get(0).content()).isEqualTo(savedKeyword.getContent());
        assertThat(result.get(0).type()).isEqualTo(savedKeyword.getType());
    }

    @Test
    @DisplayName("리뷰를 저장 할 수 있다.")
    public void findKeywordInfoSuccess() {
        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());

        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));


        Optional<Review> findReview = reviewRepository.findById(savedReview.getId());

        assertNotNull(savedReview);
        assertThat(findReview.get()).isEqualTo(savedReview);
    }

    @Test
    @DisplayName("리뷰를 업데이트 할 수 있다.")
    public void updateReviewSuccess() {

        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());

        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));

        assertThat(savedReview.getContent()).isEqualTo("saved review");

        Optional<Review> updateReview = reviewRepository.findById(savedReview.getId());

        assertNotNull(savedReview);

        updateReview.get().updateContent("update contents");
        updateReview.get().updateRating(100D);

        assertThat(updateReview.get().getContent()).isEqualTo("update contents");
        assertThat(updateReview.get().getRating()).isEqualTo(100D);

    }

    @Test
    @DisplayName("리뷰를 삭제할 수 있다.")
    public void deleteReviewSuccess() {

        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());

        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));

        assertThat(reviewRepository.findAll().size()).isEqualTo(1);

        reviewRepository.deleteById(savedReview.getId());

        assertThat(reviewRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("리뷰에서 댓글 조회를 할 수 있다.")
    public void getReviewComment(){
        Pageable pageable = PageRequest.of(0, 10);
        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));
        String content = "first comment";
        String content2 = "second comment";

        Comment comment = saveComment(content,savedReview,savedMember);
        Comment comment2 = saveComment(content2,savedReview,savedMember);

        commentRepository.saveAll(List.of(comment, comment2));

        //when
        Page<Comment> commentPage = commentRepository.findCommentsByReviewId(savedReview.getId(), pageable);

        //then
        assertThat(commentPage.getContent().size()).isEqualTo(2);
        assertThat(commentPage.getContent().get(0).getContent()).isEqualTo("first comment");
        assertThat(commentPage.getContent().get(1).getContent()).isEqualTo("second comment");
    }

}
