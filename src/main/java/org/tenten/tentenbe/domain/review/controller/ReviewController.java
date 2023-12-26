package org.tenten.tentenbe.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tenten.tentenbe.domain.comment.dto.response.CommentResponse;
import org.tenten.tentenbe.domain.review.dto.request.ReviewCreateRequest;
import org.tenten.tentenbe.domain.review.dto.request.ReviewUpdateRequest;
import org.tenten.tentenbe.domain.review.dto.response.ReviewInfo;
import org.tenten.tentenbe.domain.review.service.ReviewService;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.response.GlobalResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.DELETED;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Tag(name = "리뷰 관련 API", description = "리뷰 관련 API 모음 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;


    @Operation(summary = "리뷰 작성", description = "리뷰 작성 API 입니다.")
    @ApiResponse(responseCode = "200", description = "작성 성공시", content = @Content(schema = @Schema(implementation = ReviewInfo.class)))
    @PostMapping()
    public ResponseEntity<?> createReview(@RequestBody ReviewCreateRequest reviewCreateRequest) {
        return ResponseEntity.ok(reviewService.createReview(null, reviewCreateRequest));
    }

    @Operation(summary = "리뷰 수정", description = "리뷰 작성 API 입니다.")
    @ApiResponse(responseCode = "200", description = "업데이트 성공시", content = @Content(schema = @Schema(implementation = ReviewInfo.class)))
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @Parameter(name = "reviewId", description = "리뷰 아이디", in = PATH)
            @PathVariable("reviewId")
            Long reviewId,
            @RequestBody ReviewUpdateRequest reviewUpdateRequest) {
        return ResponseEntity.ok(reviewService.updateReview(null, reviewId,  reviewUpdateRequest));
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 삭제 API 입니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공시")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @Parameter(name = "reviewId", description = "삭제할 리뷰 아이디", in = PATH)
            @PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(null, reviewId);
        return ResponseEntity.ok(GlobalResponse.ok(DELETED));
    }

    @Operation(summary = "리뷰 댓글 조회 API", description = "리뷰 댓글 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = CommentResponse.class)))
    @GetMapping("/{reviewId}/comments")
    public ResponseEntity<?> getReviewDetail(
            @Parameter(name = "reviewId", description = "조회할 리뷰 아이디", in = PATH)
            @PathVariable("reviewId") Long reviewId) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, reviewService.getReviewComments(reviewId)));
    }
}
