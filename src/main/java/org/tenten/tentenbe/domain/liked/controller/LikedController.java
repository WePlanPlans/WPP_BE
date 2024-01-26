package org.tenten.tentenbe.domain.liked.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tenten.tentenbe.domain.liked.service.LikedService;
import org.tenten.tentenbe.global.response.GlobalResponse;

import static org.tenten.tentenbe.global.common.constant.ResponseConstant.DELETED;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;
import static org.tenten.tentenbe.global.util.SecurityUtil.getCurrentMemberId;

@Tag(name = "관심 여행지(좋아요) 관련 API", description = "관심 여행지 관련 API 모음입니다.")
@RestController
@RequestMapping("/api/liked")
@RequiredArgsConstructor
public class LikedController {

    private final LikedService likedService;

    @Operation(summary = "관심 여행지 등록 API", description = "관심 여행지 등록 API 입니다.")
    @PostMapping("/{tour_id}")
    public ResponseEntity<GlobalResponse> likeTour(
        @Parameter(description = "관심 여행지 등록할 여행지 ID", required = true, example = "1")
        @PathVariable("tour_id") Long tourId
    ) {
        likedService.likeTour(tourId, getCurrentMemberId());
        return ResponseEntity.ok(GlobalResponse.ok(SUCCESS));
    }

    @Operation(summary = "관심 여행지 삭제 API", description = "관심 여행지 삭제 API 입니다.")
    @DeleteMapping("/{tour_id}")
    public ResponseEntity<GlobalResponse> cancelLikeTour(
        @Parameter(description = "관심 여행지 삭제할 여행지 ID", required = true, example = "1")
        @PathVariable("tour_id") Long tourId
    ) {
        likedService.cancelLikeTour(tourId, getCurrentMemberId());
        return ResponseEntity.ok(GlobalResponse.ok(DELETED));
    }

}
