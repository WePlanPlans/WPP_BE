package org.tenten.tentenbe.domain.liked.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tenten.tentenbe.domain.liked.service.LikedService;
import org.tenten.tentenbe.global.response.GlobalResponse;

import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;
import static org.tenten.tentenbe.global.util.SecurityUtil.getCurrentMemberId;

@RestController
@RequestMapping("/api/liked")
@RequiredArgsConstructor
public class LikedController {

    private final LikedService likedService;

    @Operation(summary = "관심 여행지 등록/취소 API", description = "관심 여행지 등록/취소 API 입니다.")
    @PostMapping("/{tour_id}")
    public ResponseEntity<GlobalResponse> likeTour(
        @Parameter(description = "관심 여행지 등록/취소할 여행지 ID", required = true, example = "1")
        @PathVariable("tour_id") Long tourId) {
        likedService.likeTour(tourId, getCurrentMemberId());
        return ResponseEntity.ok(GlobalResponse.ok(SUCCESS));
    }

}
