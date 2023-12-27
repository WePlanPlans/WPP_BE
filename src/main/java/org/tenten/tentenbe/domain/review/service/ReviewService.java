package org.tenten.tentenbe.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.review.dto.request.ReviewCreateRequest;
import org.tenten.tentenbe.domain.review.dto.request.ReviewUpdateRequest;
import org.tenten.tentenbe.domain.review.dto.response.KeywordResponse;
import org.tenten.tentenbe.domain.review.dto.response.ReviewInfo;
import org.tenten.tentenbe.domain.review.dto.response.ReviewResponse;
import org.tenten.tentenbe.domain.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public ReviewResponse getTourReviews(Long tourId) {
        return null;
    }

    @Transactional
    public ReviewInfo createReview(Long memberId, ReviewCreateRequest reviewCreateRequest) {
        return null;
    }

    @Transactional
    public ReviewInfo updateReview(Long memberId, Long reviewId, ReviewUpdateRequest reviewUpdateRequest) {
        return null;
    }

    @Transactional
    public void deleteReview(Long memberId, Long reviewId) {

    }

    @Transactional(readOnly = true)
    public ReviewInfo getReviewComments(Long reviewId) {
        return null;
    }

    @Transactional(readOnly = true)
    public KeywordResponse getKeywords(Long code) {
        return null;
    }
}
