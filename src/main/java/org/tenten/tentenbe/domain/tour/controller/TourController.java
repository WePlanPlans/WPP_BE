package org.tenten.tentenbe.domain.tour.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import static org.tenten.tentenbe.global.util.SecurityUtil.getCurrentMemberId;

@Tag(name = "여행지 관련 API", description = "여행지 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tours")
public class TourController {
    private final TourService tourService;
    private final ReviewService reviewService;

    @Operation(summary = "인기 여행지 조회 API", description = "인기 여행지 조회 API 입니다.")
    @GetMapping()
    public ResponseEntity<GlobalDataResponse<Page<TourSimpleResponse>>> getTours(
        @Parameter(name = "region", description = "인기 여행지 조회할 지역", in = QUERY)
        @RequestParam(value = "region", required = false) String region,
        @Parameter(name = "page", description = "페이지 번호", in = QUERY)
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = QUERY)
        @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS,
            tourService.getTours(getCurrentMemberId(), region, PageRequest.of(page, size))));
    }

    @Operation(summary = "여행지 검색 API", description = "여행지 검색 API 입니다.")
    @GetMapping("/search")
    public ResponseEntity<GlobalDataResponse<Page<TourSimpleResponse>>> searchTours(
        @Parameter(name = "region", description = "검색할 지역", in = QUERY)
        @RequestParam(value = "region", required = false) String region,
        @Parameter(name = "category", description = "검색할 여행 상품 타입, 미지정 가능", in = QUERY)
        @RequestParam(value = "category", required = false) String category,
        @Parameter(name = "searchWord", description = "검색할 상품명", in = QUERY, required = true)
        @RequestParam(value = "searchWord", required = true) String searchWord,
        @Parameter(name = "page", description = "페이지 번호", in = QUERY)
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = QUERY)
        @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS,
            tourService.searchTours(getCurrentMemberId(), region, category, searchWord, PageRequest.of(page, size))));
    }

    @Operation(summary = "여행지 상세 조회 API", description = "여행지 상세 조회 API 입니다.")
    @GetMapping("/{tourItemId}")
    public ResponseEntity<GlobalDataResponse<TourDetailResponse>> getTourDetail(
        @Parameter(name = "tourItemId", description = "상세조회할 여행 상품 ID", in = PATH, required = true)
        @PathVariable(value = "tourItemId") Long tourItemId
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS,
            tourService.getTourDetail(getCurrentMemberId(), tourItemId)));
    }

    @Operation(summary = "여행 상품 리뷰 조회 API", description = "여행 상품 리뷰 & 키워드 조회 API 입니다")
    @GetMapping("/{tourItemId}/reviews")
    public ResponseEntity<GlobalDataResponse<ReviewResponse>> getTourReviews(
        @PathVariable(name = "tourItemId") Long tourItemId,
        @Parameter(name = "page", description = "페이지 번호", in = QUERY)
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = QUERY)
        @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS,
            reviewService.getTourReviews(tourItemId, PageRequest.of(page, size), getCurrentMemberId())));
    }

}
