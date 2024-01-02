package org.tenten.tentenbe.domain.trip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tenten.tentenbe.domain.trip.dto.request.TripCreateRequest;
import org.tenten.tentenbe.domain.trip.dto.response.TripCreateResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse;
import org.tenten.tentenbe.domain.trip.service.TripService;
import org.tenten.tentenbe.global.common.constant.ResponseConstant;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.response.GlobalResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.DELETED;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Tag(name = "여정 관련 API", description = "여정 관련 API 모음입니다. 소켓 통신 문서화는 노션에 별도로 작성해놓겠습니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trips")
public class TripController {
    private final TripService tripService;

    @Operation(summary = "여정 생성 API", description = "여정 생성 API 입니다. 지역 정보는 지역 정보 조회 API를 호출한 후에, 유효한 지역 명을 넣어주셔야합니다.")
    @PostMapping()
    public ResponseEntity<GlobalDataResponse<TripCreateResponse>> createTrip(@RequestBody TripCreateRequest tripCreateRequest) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, tripService.createTrip(1L, tripCreateRequest)));
    }

    @Operation(summary = "나의 여정 목록 조회 API", description = "자신의 여정 목록 조회 API 입니다.")
    @GetMapping()
    public ResponseEntity<GlobalDataResponse<TripResponse>> getTrips(
        @Parameter(name = "page", description = "페이지 번호", in = QUERY, required = false)
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = QUERY, required = false)
        @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Long memberId = 1L;
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, tripService.getTrips(memberId, PageRequest.of(page, size))));
    }

    @Operation(summary = "여정 탈퇴 API", description = "본인이 속한 여정에서 나가는 API 입니다.")
    @DeleteMapping("/{tripId}")
    public ResponseEntity<GlobalResponse> deleteTripMember(
        @Parameter(name = "tripId", description = "탈퇴할 여정 아이디", in = PATH)
        @PathVariable(name = "tripId")
        Long tripId
    ) {
        tripService.deleteTripMember(null, tripId);
        return ResponseEntity.ok(GlobalResponse.ok(DELETED));
    }



}
