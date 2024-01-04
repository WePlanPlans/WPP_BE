package org.tenten.tentenbe.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tenten.tentenbe.domain.member.dto.request.MemberUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.response.MemberDetailResponse;
import org.tenten.tentenbe.domain.member.dto.response.MemberUpdateResponse;
import org.tenten.tentenbe.domain.member.service.MemberService;
import org.tenten.tentenbe.domain.review.dto.response.MemberReviewResponse;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.response.GlobalResponse;
import org.tenten.tentenbe.global.s3.ImageUploadDto;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.DELETED;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;
import static org.tenten.tentenbe.global.util.SecurityUtil.getCurrentMemberId;

@Tag(name = "유저 관련 API", description = "유저 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "나의 여정 조회 API", description = "나의 여정 조회 API 입니다.")
    @GetMapping("/trips")
    public ResponseEntity<GlobalDataResponse<Page<TripSimpleResponse>>> getTrips(
        @Parameter(name = "page", description = "페이지 번호", in = QUERY, required = false)
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = QUERY, required = false)
        @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Long memberId = 1L;
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, memberService.getTrips(memberId, PageRequest.of(page, size))));
    }

    @Operation(summary = "나의 관심 여행지 조회 API", description = "나의 관심 여행지 조회 API 입니다.")
    @GetMapping("/tours")
    public ResponseEntity<GlobalDataResponse<Page<TourSimpleResponse>>> getTours(
        @Parameter(name = "page", description = "페이지 번호", in = QUERY, required = false)
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = QUERY, required = false)
        @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(
            SUCCESS, memberService.getTours(getCurrentMemberId(), PageRequest.of(page, size))));
    }

    @Operation(summary = "나의 리뷰 조회 API", description = "나의 리뷰 조회 API 입니다.")
    @GetMapping("/reviews")
    public ResponseEntity<GlobalDataResponse<Page<MemberReviewResponse>>> getReviews(
        @Parameter(name = "page", description = "페이지 번호", in = QUERY, required = false)
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = QUERY, required = false)
        @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(
            SUCCESS, memberService.getReviews(getCurrentMemberId(), PageRequest.of(page, size))));
    }

    @Operation(summary = "회원 정보 조회 API", description = "회원 정보 조회 API 입니다.")
    @GetMapping()
    public ResponseEntity<GlobalDataResponse<MemberDetailResponse>> getMemberInfo() {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, memberService.getMemberInfo(getCurrentMemberId())));
    }

    @Operation(summary = "회원 정보 수정 API", description = "회원 정보 수정 API 입니다.")
    @PutMapping()
    public ResponseEntity<GlobalDataResponse<MemberUpdateResponse>> updateMember(
        @RequestBody MemberUpdateRequest memberUpdateRequest
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(
            SUCCESS, memberService.updateMember(getCurrentMemberId(), memberUpdateRequest)));
    }

    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API 입니다.")
    @DeleteMapping()
    public ResponseEntity<GlobalResponse> deleteMember() {
        memberService.deleteMember(getCurrentMemberId());
        return ResponseEntity.ok(GlobalResponse.ok(DELETED));
    }

    @Operation(
        summary = "프로필 이미지 업로드 API",
        description = "MultipartFile 형태의 이미지 파일을 'images'라는 키로 form-data 형태로 전송해주세요. 이 API는 전송된 이미지를 S3에 저장하고, 저장된 이미지의 URL을 반환합니다."
    )
    @PostMapping(value = "",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GlobalDataResponse<ImageUploadDto>> uploadImage(
        @RequestParam("images") MultipartFile multipartFile) throws BadRequestException {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, memberService.uploadImage(multipartFile)));
    }

}
