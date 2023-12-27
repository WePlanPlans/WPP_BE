package org.tenten.tentenbe.domain.tour.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tenten.tentenbe.domain.review.dto.response.ReviewResponse;
import org.tenten.tentenbe.domain.review.service.ReviewService;
import org.tenten.tentenbe.domain.tour.dto.response.TourDetailResponse;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.tour.service.TourService;
import org.tenten.tentenbe.global.response.GlobalDataResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Tag(name = "여행지 관련 API", description = "여행지 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tours")
public class TourController {
    private final TourService tourService;
    private final ReviewService reviewService;

    @Operation(summary = "인기 여행지 조회 API", description = "인기 여행지 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content =
    @Content(schema = @Schema(implementation = TourSimpleResponse.class)))
    @GetMapping()
    public ResponseEntity<?> getTours(
        @Parameter(name = "region", description = "인기 여행지 조회할 지역", in = QUERY, required = false)
        @RequestParam(value = "region", required = false) String region) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, tourService.getTours(region)));

    }

    @Operation(summary = "여행지 검색 API", description = "여행지 검색 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content =
    @Content(schema = @Schema(implementation = TourSimpleResponse.class)))
    @GetMapping("/search")
    public ResponseEntity<?> searchTours(
        @Parameter(name = "region", description = "검색할 지역", in = QUERY, required = true)
        @RequestParam(value = "region", required = true) String region,
        @Parameter(name = "type", description = "검색할 여행 상품 타입, 미지정 가능", in = QUERY, required = false)
        @RequestParam(value = "type", required = false) String type,
        @Parameter(name = "keyword", description = "검색할 상품명", in = QUERY, required = true)
        @RequestParam(value = "keyword", required = true) String keyword
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, tourService.searchTours(region, type, keyword)));
    }

    @Operation(summary = "여행지 상세 조회 API", description = "여행지 상세 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content =
    @Content(schema = @Schema(implementation = TourDetailResponse.class)))
    @GetMapping("/{tourId}")
    public ResponseEntity<?> getTourDetail(
        @Parameter(name = "tourId", description = "상세조회할 여행 상품 ID", in = PATH, required = true)
        @PathVariable(value = "tourId") Long tourId
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, tourService.getTourDetail(tourId, null)));
    }

    @Operation(summary = "여행 상품 리뷰 조회", description = "여행 상품 리뷰 & 키워드 조회 API 입니다")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content =
    @Content(schema = @Schema(implementation = ReviewResponse.class)))
    @GetMapping("/{tourId}/reviews")
    public ResponseEntity<?> getTourReviews(@PathVariable(name = "tourId") Long tourId) {
        return ResponseEntity.ok(reviewService.getTourReviews(tourId));
    }

}
