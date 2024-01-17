package org.tenten.tentenbe.domain.trip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tenten.tentenbe.domain.trip.dto.request.JoinTripRequest;
import org.tenten.tentenbe.domain.trip.dto.request.TripCreateRequest;
import org.tenten.tentenbe.domain.trip.dto.request.TripInfoUpdateRequest;
import org.tenten.tentenbe.domain.trip.dto.request.TripLikedItemRequest;
import org.tenten.tentenbe.domain.trip.dto.response.*;
import org.tenten.tentenbe.domain.trip.service.TripService;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.response.GlobalResponse;

import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.DELETED;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;
import static org.tenten.tentenbe.global.util.SecurityUtil.*;

@Tag(name = "여정 관련 API", description = "여정 관련 API 모음입니다. 소켓 통신 문서화는 노션에 별도로 작성해놓겠습니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trips")
public class TripController {
    private final TripService tripService;

    @Operation(summary = "여정 생성 API", description = "여정 생성 API 입니다.")
    @PostMapping()
    public ResponseEntity<GlobalDataResponse<TripCreateResponse>> createTrip(@RequestBody TripCreateRequest tripCreateRequest) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, tripService.createTrip(getCurrentMemberId(), tripCreateRequest)));
    }

    @Operation(summary = "나의 여정목록 조회 API", description = "나의 여정목록 조회 API 입니다.")
    @GetMapping()
    public ResponseEntity<GlobalDataResponse<List<TripSimpleResponse>>> getTrips() {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, tripService.getTrips(getCurrentMemberId())));
    }

    @Operation(summary = "여정 상세조회 API", description = "여정 상세조회 API 입니다.")
    @GetMapping("/{tripId}")
    public ResponseEntity<GlobalDataResponse<TripDetailResponse>> getTrip(
        @Parameter(name = "tripId", description = "상세 조회할 여정 아이디", in = PATH)
        @PathVariable(name = "tripId") Long tripId
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, tripService.getTrip(tripId)));
    }

    @Operation(summary = "여정 기본정보 수정 API", description = "여정 기본정보 수정 API 입니다.")
    @PutMapping("/{tripId}")
    public ResponseEntity<GlobalDataResponse<TripInfoUpdateResponse>> updateTrip(
        @Parameter(name = "tripId", description = "기본정보를 수정할 여정 아이디", in = PATH)
        @PathVariable(name = "tripId") Long tripId,
        @RequestBody TripInfoUpdateRequest tripInfoUpdateRequest
    ) {
        return ResponseEntity.ok(GlobalDataResponse
            .ok(SUCCESS, tripService.updateTrip(getCurrentMemberId(), tripId, tripInfoUpdateRequest)));
    }

    @Operation(summary = "여정 탈퇴 API", description = "본인이 속한 여정에서 나가는 API 입니다.")
    @DeleteMapping("/{tripId}")
    public ResponseEntity<GlobalResponse> deleteTripMember(
        @Parameter(name = "tripId", description = "탈퇴할 여정 아이디", in = PATH)
        @PathVariable(name = "tripId") Long tripId
    ) {
        tripService.deleteTripMember(getCurrentMemberId(), tripId);
        return ResponseEntity.ok(GlobalResponse.ok(DELETED));
    }

    @Operation(summary = "우리의 관심 여행지 등록 API", description = "우리의 관심 여행지 등록 API 입니다.")
    @PostMapping("/{tripId}/tripLikedTours")
    public ResponseEntity<GlobalResponse> LikeTourInOurTrip(
        @Parameter(name = "tripId", description = "우리의 관심 여행지를 등록할 여정 아이디", in = PATH)
        @PathVariable(name = "tripId") Long tripId,
        @RequestBody TripLikedItemRequest request
    ) {
        tripService.LikeTourInOurTrip(getCurrentMemberId(), tripId, request);
        return ResponseEntity.ok(GlobalResponse.ok(SUCCESS));
    }

    @Operation(summary = "우리의 관심목록 조회 API", description = "우리의 관심목록 조회 API 입니다.")
    @GetMapping("/{tripId}/tripLikedTours")
    public ResponseEntity<GlobalDataResponse<Page<TripLikedSimpleResponse>>> getTripLikedTours(
        @Parameter(name = "tripId", description = "우리의 관심목록을 조회할 여정 아이디", in = PATH)
        @PathVariable(name = "tripId") Long tripId,
        @Parameter(name = "category", description = "우리의 관심목록을 조회할 카테고리", in = QUERY, required = false)
        @RequestParam(value = "category", required = false) String category,
        @Parameter(name = "page", description = "페이지 번호", in = QUERY, required = false)
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = QUERY, required = false)
        @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(GlobalDataResponse
            .ok(SUCCESS, tripService.getTripLikedItems(getCurrentMemberId(), tripId, category, PageRequest.of(page, size)))
        );
    }

    @Operation(summary = "우리의 관심 여행지 좋아요/싫어요 API", description = "우리의 관심 여행지 좋아요/싫어요 API 입니다.")
    @PostMapping("/{tripId}/tripLikedTours/{tourId}")
    public ResponseEntity<GlobalResponse> preferOrNotTourInOurTrip(
        @Parameter(name = "tripId", description = "우리의 관심 여행지 좋아요/싫어요할 여정 아이디", in = PATH)
        @PathVariable("tripId") Long tripId,
        @Parameter(name = "tourId", description = "우리의 관심 여행지 좋아요/싫어요할 여행지 아이디", in = PATH)
        @PathVariable("tourId") Long tourId,
        @Parameter(name = "prefer", description = "선호", in = QUERY)
        @RequestParam("prefer") Boolean prefer,
        @Parameter(name = "notPrefer", description = "비선호", in = QUERY)
        @RequestParam("notPrefer") Boolean notPrefer
    ) {
        tripService.preferOrNotTourInOurTrip(getCurrentMemberId(), tripId, tourId, prefer, notPrefer);
        return ResponseEntity.ok(GlobalResponse.ok(SUCCESS));
    }

    @Operation(summary = "우리의 여행취향 조회 API", description = "우리의 여행취향 조회 API 입니다.")
    @GetMapping("/{tripId}/survey")
    public ResponseEntity<GlobalDataResponse<TripSurveyResponse>> getTripSurveys(
        @Parameter(name = "tripId", description = "우리의 여행취향을 조회할 여정 아이디", in = PATH)
        @PathVariable(name = "tripId") Long tripId
    ) {
        return ResponseEntity.ok(GlobalDataResponse
            .ok(SUCCESS, tripService.getTripSurveys(tripId))
        );
    }

    @Operation(summary = "우리의 여행취향 참여/미참여 회원 조회 API", description = "우리의 여행취향 참여/미참여 회원 조회 API 입니다.")
    @GetMapping("/{tripId}/survey/members")
    public ResponseEntity<GlobalDataResponse<TripSurveyMemberResponse>> getTripSurveyMembers(
        @Parameter(name = "tripId", description = "우리의 여행취향 참여/미참여 회원을 조회할 여정 아이디", in = PATH)
        @PathVariable(name = "tripId") Long tripId
    ) {
        return ResponseEntity.ok(GlobalDataResponse
            .ok(SUCCESS, tripService.getTripSurveyMember(tripId))
        );
    }

    @Operation(summary = "여정 참여 API", description = "참여코드를 이용한 여정 참여 API 입니다.")
    @PostMapping("/{tripId}/join")
    public ResponseEntity<GlobalDataResponse<Long>> joinTrip(
        @Parameter(name = "tripId", description = "참여할 여정 아이디", in = PATH)
        @PathVariable(name = "tripId") Long tripId,
        @RequestBody JoinTripRequest joinTripRequest
    ) {
        tripService.joinTrip(getCurrentMemberId(), tripId, joinTripRequest.participantCode());
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, tripId));
    }

    @Operation(summary = "여정 참여코드 조회 API", description = "해당 여정의 참여 코드를 조회합니다.")
    @GetMapping("/{tripId}/join")
    public ResponseEntity<GlobalDataResponse<String>> joinTrip(
        @Parameter(name = "tripId", description = "참여코드를 조회할 여정 아이디", in = PATH)
        @PathVariable(name = "tripId") Long tripId
    ) {
        return ResponseEntity.ok(GlobalDataResponse
            .ok(SUCCESS, tripService.getJoinCode(getCurrentMemberId(), tripId)));
    }

    @Operation(summary = "여정을 공유하고 있는 회원 조회 API", description = "여정을 공유하고 있는 회원을 모두 조회합니다.")
    @GetMapping("/{tripId}/members")
    public ResponseEntity<GlobalDataResponse<TripMembersResponse>> getTripMembers(
        @Parameter(name = "tripId", description = "여정을 공유하고 있는 회원을 조회할 여정 아이디", in = PATH)
        @PathVariable(name = "tripId") Long tripId
    ) {
        return ResponseEntity.ok(GlobalDataResponse
            .ok(SUCCESS, tripService.getTripMembers(tripId)));
    }
}
