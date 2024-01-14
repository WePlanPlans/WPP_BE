package org.tenten.tentenbe.common.fixture;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.review.dto.request.ReviewCreateRequest;
import org.tenten.tentenbe.domain.review.dto.request.ReviewKeywordCreateRequest;
import org.tenten.tentenbe.domain.review.dto.request.ReviewUpdateRequest;
import org.tenten.tentenbe.domain.review.dto.response.*;
import org.tenten.tentenbe.domain.review.model.Keyword;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.review.model.ReviewKeyword;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import java.util.List;

import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.global.common.enums.KeywordType.DINING_KEYWORD;

public class ReviewFixture {

    public static Review review() {
        return Review.builder()
                .id(1L)
                .rating(20.5)
                .content("hello world")
                .tourItem(tourItem())
                .creator(newBasicMember())
                .build();
    }

    public static Review updateReview() {
        return Review.builder()
                .id(1L)
                .rating(20.5)
                .content("update Review")
                .tourItem(tourItem())
                .creator(newBasicMember())
                .build();
    }

    public static Review saveReview(TourItem tourItem, Member member) {
        return Review.builder()
                .id(1L)
                .rating(20.5)
                .content("saved review")
                .tourItem(tourItem)
                .creator(member)
                .build();
    }

    public static ReviewInfo saveReviewInfo(Review review, Member member) {
        return ReviewInfo.fromEntity(review, member.getId());
    }

    public static ReviewInfo reviewInfo() {
        return ReviewInfo.fromEntity(review(), newBasicMember().getId());
    }


    public static ReviewKeywordCreateRequest reviewKeywordCreateRequest() {
        return new ReviewKeywordCreateRequest(1L, "review Key Word");
    }

    public static ReviewCreateRequest reviewCreateRequest() {
        return new ReviewCreateRequest(1L, 2.5D, List.of(reviewKeywordCreateRequest()), "review content test");
    }

    public static ReviewUpdateRequest reviewUpdateRequest() {
        return new ReviewUpdateRequest(42.3, List.of(reviewKeywordCreateRequest()), "Review Update Content");
    }


    public static ReviewResponse reviewResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ReviewInfo> reviewInfos = List.of(ReviewInfo.fromEntity(review(), AuthFixture.newBasicMember().getId()));
        return
                new ReviewResponse(
                        20.2,
                        tourItem().getReviewTotalCount(),
                        15L,
                        new PageImpl<>(reviewInfos, pageable, reviewInfos.size()),
                        List.of(new TourKeywordInfo(1L, "깨끗해요", DINING_KEYWORD, 2L))
                );

    }

    public static Keyword keyword() {
        return new Keyword(1L, "save keyword content", DINING_KEYWORD);
    }

    public static KeywordInfo keywordInfo() {
        return KeywordInfo.fromEntity(keyword());
    }

    public static KeywordResponse keywordResponse() {
        return new KeywordResponse(List.of(keywordInfo()));
    }

    public static TourKeywordInfo tourKeywordInfo() {
        return new TourKeywordInfo(1L, "tour keyword info test", DINING_KEYWORD, 10L);
    }

    public static ReviewKeyword reviewKeyword() {
        return new ReviewKeyword(1L, review(), keyword());
    }

    public static ReviewKeyword saveReviewKeyword(Review review,Keyword keyword) {
        return ReviewKeyword.builder()
                .keyword(keyword)
                .review(review)
                .build();
    }

}
