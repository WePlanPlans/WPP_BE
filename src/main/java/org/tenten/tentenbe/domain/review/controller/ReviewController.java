package org.tenten.tentenbe.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tenten.tentenbe.domain.comment.dto.response.CommentResponse;
import org.tenten.tentenbe.domain.review.dto.request.ReviewCreateRequest;
import org.tenten.tentenbe.domain.review.dto.request.ReviewUpdateRequest;
import org.tenten.tentenbe.domain.review.dto.response.KeywordResponse;
import org.tenten.tentenbe.domain.review.dto.response.ReviewInfo;
import org.tenten.tentenbe.domain.review.service.ReviewService;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.response.GlobalResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.DELETED;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;
import static org.tenten.tentenbe.global.util.SecurityUtil.getCurrentMemberId;

@Tag(name = "리뷰 관련 API", description = "리뷰 관련 API 모음 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;


    @Operation(summary = "리뷰 작성 API", description = "리뷰 작성 API 입니다.")
    @PostMapping()
    public ResponseEntity<GlobalDataResponse<ReviewInfo>> createReview(@RequestBody ReviewCreateRequest reviewCreateRequest) {
        Long currentMemberId = getCurrentMemberId();
        if (currentMemberId == null) {
            currentMemberId = 12L;
        }
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, reviewService.createReview(currentMemberId, reviewCreateRequest)));
    }

    @Operation(summary = "리뷰 수정 API", description = "리뷰 수정 API 입니다.")
    @PutMapping("/{reviewId}")
    public ResponseEntity<GlobalDataResponse<ReviewInfo>> updateReview(
        @Parameter(name = "reviewId", description = "리뷰 아이디", in = PATH)
        @PathVariable("reviewId")
        Long reviewId,
        @RequestBody ReviewUpdateRequest reviewUpdateRequest) {
        Long currentMemberId = getCurrentMemberId();
        if (currentMemberId == null) {
            currentMemberId = 12L;
        }
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, reviewService.updateReview(currentMemberId, reviewId, reviewUpdateRequest)));
    }

    @Operation(summary = "리뷰 삭제 API", description = "리뷰 삭제 API 입니다.")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<GlobalResponse> deleteReview(
        @Parameter(name = "reviewId", description = "삭제할 리뷰 아이디", in = PATH)
        @PathVariable("reviewId") Long reviewId) {
        Long currentMemberId = getCurrentMemberId();
        if (currentMemberId == null) {
            currentMemberId = 12L;
        }
        reviewService.deleteReview(currentMemberId, reviewId);
        return ResponseEntity.ok(GlobalResponse.ok(DELETED));
    }

    @Operation(summary = "리뷰 댓글 조회 API", description = "리뷰 댓글 조회 API 입니다.")
    @GetMapping("/{reviewId}/comments")
    public ResponseEntity<GlobalDataResponse<CommentResponse>> getReviewDetail(
        @Parameter(name = "reviewId", description = "조회할 리뷰 아이디", in = PATH)
        @PathVariable("reviewId") Long reviewId,
        @Parameter(name = "page", description = "페이지 번호", in = QUERY, required = false)
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = QUERY, required = false)
        @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, reviewService.getReviewComments(reviewId, PageRequest.of(page, size), getCurrentMemberId())));
    }

    @Operation(summary = "리뷰 작성시 키워드 조회 API", description = "리뷰 작성시, 전체 키워드 혹은 조회 하고 싶은 키워드 타입별 키워드 목록 조회 API 입니다.")
    @GetMapping("/keywords")
    public ResponseEntity<GlobalDataResponse<KeywordResponse>> getKeywords(
        @Parameter(name = "keywordType", description = "조회하고 싶은 키워드 타입, ex) ACCOMMODATION - 숙박, DINING - 식당, ATTRACTION - 관광지", in = QUERY)
        @RequestParam(name = "keywordType", required = false)
        String keywordType
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, reviewService.getKeywords(keywordType)));
    }
}
