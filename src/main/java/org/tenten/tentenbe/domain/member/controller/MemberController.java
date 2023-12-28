package org.tenten.tentenbe.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tenten.tentenbe.domain.member.dto.request.MemberUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.response.MemberResponse;
import org.tenten.tentenbe.domain.member.service.MemberService;
import org.tenten.tentenbe.domain.review.dto.response.MemberReviewResponse;
import org.tenten.tentenbe.domain.review.dto.response.ReviewResponse;
import org.tenten.tentenbe.domain.tour.dto.response.TourResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripResponse;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.response.GlobalResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.DELETED;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Tag(name = "유저 관련 API", description = "유저 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "나의 여정 조회 API", description = "나의 여정 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "여정 조회 성공시", content = @Content(schema = @Schema(implementation = TripResponse.class)))
    @GetMapping("/trips")
    public ResponseEntity<?> getTrips() {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, memberService.getTrips(null)));
    }

    @Operation(summary = "나의 관심 여행지 조회 API", description = "나의 관심 여행지 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "관심 여행지 조회 성공시", content = @Content(schema = @Schema(implementation = TourResponse.class)))
    @GetMapping("/tours")
    public ResponseEntity<?> getTours() {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, memberService.getTours(null)));
    }

    @Operation(summary = "나의 관심 여행지 삭제 API", description = "나의 관심 여행지 삭제 API 입니다.")
    @ApiResponse(responseCode = "200", description = "관심 여행지 삭제 성공시")
    @DeleteMapping("/tour/{tourId}")
    public ResponseEntity<?> deleteTour(
        @Parameter(name = "tourId", description = "삭제할 여행지 아이디", in = PATH)
        @PathVariable("tourId") Long tourId
    ) {
        memberService.deleteTour(null, tourId);
        return ResponseEntity.ok(GlobalResponse.ok(DELETED));
    }

    @Operation(summary = "나의 리뷰 조회 API", description = "나의 리뷰 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 조회 성공시", content = @Content(schema = @Schema(implementation = MemberReviewResponse.class)))
    @GetMapping("/reviews")
    public ResponseEntity<?> getReviews() {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, memberService.getReviews(null)));
    }

    @Operation(summary = "회원 정보 조회 API", description = "회원 정보 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공시", content = @Content(schema = @Schema(implementation = MemberResponse.class)))
    @GetMapping()
    public ResponseEntity<?> getMemberInfo() {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, memberService.getMemberInfo(null)));
    }

    @Operation(summary = "회원 정보 수정 API", description = "회원 정보 수정 API 입니다.")
    @ApiResponse(responseCode = "200", description = "업데이트 성공시", content = @Content(schema = @Schema(implementation = MemberResponse.class)))
    @PutMapping()
    public ResponseEntity<?> updateMember(
        @RequestBody MemberUpdateRequest memberUpdateRequest
        ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, memberService.updateMember(null, memberUpdateRequest)));
    }

    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API 입니다.")
    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공시")
    @DeleteMapping()
    public ResponseEntity<?> deleteMember() {
        memberService.deleteMember(null);
        return ResponseEntity.ok(GlobalResponse.ok(DELETED));
    }
}
