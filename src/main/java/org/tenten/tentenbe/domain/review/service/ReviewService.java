package org.tenten.tentenbe.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.comment.dto.response.CommentInfo;
import org.tenten.tentenbe.domain.comment.dto.response.CommentResponse;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.comment.repository.CommentRepository;
import org.tenten.tentenbe.domain.member.exception.MemberException;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.review.dto.request.ReviewCreateRequest;
import org.tenten.tentenbe.domain.review.dto.request.ReviewKeywordCreateRequest;
import org.tenten.tentenbe.domain.review.dto.request.ReviewUpdateRequest;
import org.tenten.tentenbe.domain.review.dto.response.*;
import org.tenten.tentenbe.domain.review.exception.KeywordException;
import org.tenten.tentenbe.domain.review.exception.ReviewException;
import org.tenten.tentenbe.domain.review.model.Keyword;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.review.model.ReviewKeyword;
import org.tenten.tentenbe.domain.review.repository.KeywordRepository;
import org.tenten.tentenbe.domain.review.repository.ReviewKeywordRepository;
import org.tenten.tentenbe.domain.review.repository.ReviewRepository;
import org.tenten.tentenbe.domain.tour.exception.TourException;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;
import org.tenten.tentenbe.global.common.enums.KeywordType;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final TourItemRepository tourItemRepository;
    private final KeywordRepository keywordRepository;
    private final ReviewKeywordRepository reviewKeywordRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public ReviewResponse getTourReviews(Long tourItemId, Pageable pageable, Long memberId) {
        TourItem tourItem = getTourItem(tourItemId);
        Page<Review> reviewPage = reviewRepository.findReviewByTourItemId(tourItem.getId(), pageable);
        List<ReviewInfo> reviewInfos = reviewPage.stream().map(r -> ReviewInfo.fromEntity(r, memberId)).toList();

        Long keywordTotalCount = keywordRepository.findKeywordInfoByTourItemIdAndKeywordType(tourItem.getId()).stream()
            .mapToLong(TourKeywordInfo::keywordCount)
            .sum();
        Double ratingAverage = tourItem.getReviews().stream()
            .mapToDouble(Review::getRating)
            .average()
            .orElse(0.0);

        return new ReviewResponse(
            ratingAverage,
            tourItem.getReviewTotalCount(),
            keywordTotalCount,
            new PageImpl<>(reviewInfos, pageable, tourItem.getReviewTotalCount()),
            keywordRepository.findKeywordInfoByTourItemIdAndKeywordType(tourItem.getId())
        );
    }

    @Transactional
    public ReviewInfo createReview(Long memberId, ReviewCreateRequest reviewCreateRequest) {
        Member member = getMember(memberId);
        TourItem tourItem = getTourItem(reviewCreateRequest.tourItemId());
        Review review = reviewCreateRequest.toEntity(member);

        tourItem.increaseReviewCount();
        reviewRepository.save(review);

        List<ReviewKeyword> reviewKeywords = addKeywordToReview(review, reviewCreateRequest.keywords());

        return ReviewInfo.fromEntity(review, reviewKeywords, memberId);
    }

    @Transactional
    public ReviewInfo updateReview(Long memberId, Long reviewId, ReviewUpdateRequest reviewUpdateRequest) {
        Member member = getMember(memberId);
        Review review = getReview(reviewId);
        validateCreator(member, review);

        review.updateContent(reviewUpdateRequest.content());
        review.updateRating(reviewUpdateRequest.rating());

        reviewKeywordRepository.deleteByReviewId(reviewId);
        List<ReviewKeyword> reviewKeywords = addKeywordToReview(review, reviewUpdateRequest.keywords());

        return ReviewInfo.fromEntity(review, reviewKeywords, memberId);
    }

    private List<ReviewKeyword> addKeywordToReview(Review review, List<ReviewKeywordCreateRequest> keywords) {
        List<ReviewKeyword> reviewKeywords = keywords.stream().map(keyWordRequest -> {
            Keyword keyword = keywordRepository.findById(keyWordRequest.keywordId())
                .orElseThrow(() -> new KeywordException(
                    "해당 아이디로 존재하는 키워드가 없습니다. keywordId : " + keyWordRequest.keywordId(), NOT_FOUND));

            return ReviewKeyword.builder()
                .review(review)
                .keyword(keyword)
                .build();
        }).toList();
        reviewKeywordRepository.saveAll(reviewKeywords);

        return reviewKeywords;
    }

    @Transactional
    public void deleteReview(Long memberId, Long reviewId) {
        Member member = getMember(memberId);
        Review review = getReview(reviewId);

        validateCreator(member, review);
        TourItem tourItem = review.getTourItem();
        tourItem.decreaseReviewCount();
        reviewRepository.deleteById(review.getId());
    }

    @Transactional(readOnly = true)
    public CommentResponse getReviewComments(Long reviewId, Pageable pageable, Long memberId) {
        Page<Comment> comments = commentRepository.findCommentsByReviewId(reviewId, pageable);
        List<CommentInfo> commentInfos = comments.stream()
            .map(comment -> new CommentInfo(
                comment.getId(),
                comment.getCreator().getNickname(),
                comment.getCreator().getProfileImageUrl(),
                comment.getContent(), comment.getCreatedTime(),
                comment.getCreator().getId().equals(memberId))
            ).toList();

        return new CommentResponse(new PageImpl<>(commentInfos, pageable, comments.getTotalElements()));
    }

    @Transactional(readOnly = true)
    public KeywordResponse getKeywords(String keywordTypeName) {
        if (keywordTypeName == null) {
            return new KeywordResponse(keywordRepository.findAll().stream().map(KeywordInfo::fromEntity).toList());
        } else {
            KeywordType keywordType = KeywordType.fromName(keywordTypeName);
            return new KeywordResponse(
                keywordRepository.findByType(keywordType).stream().map(KeywordInfo::fromEntity).toList());
        }
    }

    private TourItem getTourItem(Long tourItemId) {
        return tourItemRepository.findById(tourItemId).orElseThrow(
            () -> new TourException("해당 아이디로 존재하는 리뷰가 없습니다. tourItemId : " + tourItemId, NOT_FOUND));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
            () -> new MemberException("해당 아이디로 존재하는 멤버가 없습니다. memberId : " + memberId, NOT_FOUND));
    }

    private Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
            () -> new ReviewException("해당 아이디로 존재하는 리뷰가 없습니다. reviewId : " + reviewId, NOT_FOUND));
    }

    private void validateCreator(Member member, Review review) {
        if (!member.getId().equals(review.getCreator().getId())) {
            throw new ReviewException("작성하지 않은 리뷰로 편집 권한이 없습니다.", CONFLICT);
        }
    }
}
