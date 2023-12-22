package org.tenten.tentenbe.domain.region.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tenten.tentenbe.domain.region.dto.response.RegionResponse;
import org.tenten.tentenbe.domain.region.service.RegionService;
import org.tenten.tentenbe.global.response.GlobalDataResponse;

import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Tag(name = "지역 관련 API", description = "지역 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/region")
public class RegionController {
    private final RegionService regionService;

    @Operation(summary = "전체 지역 조회 API", description = "전체 지역 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = RegionResponse.class)))
    @GetMapping("")
    public ResponseEntity<?> getRegions() {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, regionService.getRegions()));
    }

    @Operation(summary = "인기 지역 조회 API", description = "인기 여행지 지역 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = RegionResponse.class)))
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularRegions() {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, regionService.getPopularRegions()));
    }
}
