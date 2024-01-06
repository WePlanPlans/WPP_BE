package org.tenten.tentenbe.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tenten.tentenbe.domain.comment.dto.request.CommentCreateRequest;
import org.tenten.tentenbe.domain.comment.dto.request.CommentUpdateRequest;
import org.tenten.tentenbe.domain.comment.dto.response.CommentInfo;
import org.tenten.tentenbe.domain.comment.dto.response.CommentResponse;
import org.tenten.tentenbe.domain.comment.service.CommentService;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.response.GlobalResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.DELETED;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;
import static org.tenten.tentenbe.global.util.SecurityUtil.getCurrentMemberId;

@Tag(name = "댓글 관련 API", description = "댓글 관련 API 모음 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성 API", description = "댓글 작성 API 입니다.")
    @PostMapping()
    public ResponseEntity<GlobalDataResponse<CommentInfo>> createComment(
        @RequestBody CommentCreateRequest commentCreateRequest
    ) {
        Long currentMemberId = getCurrentMemberId();
        if (currentMemberId == null) {
            currentMemberId = 12L;
        }
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, commentService.createComment(currentMemberId, commentCreateRequest)));
    }

    @Operation(summary = "댓글 수정 API", description = "댓글 수정 API 입니다.")
    @PutMapping("/{commentId}")
    public ResponseEntity<GlobalDataResponse<CommentInfo>> updateComment(
        @Parameter(name = "commentId", description = "댓글 아이디", in = PATH)
        @PathVariable("commentId")
        Long commentId,
        @RequestBody CommentUpdateRequest commentUpdateRequest
    ) {
        Long currentMemberId = getCurrentMemberId();
        if (currentMemberId == null) {
            currentMemberId = 12L;
        }
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, commentService.updateComment(currentMemberId, commentId, commentUpdateRequest)));
    }

    @Operation(summary = "댓글 삭제 API", description = "댓글 삭제 API 입니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<GlobalResponse> deleteComment(
        @Parameter(name = "commentId", description = "댓글 아이디", in = PATH)
        @PathVariable("commentId")
        Long commentId
    ) {
        Long currentMemberId = getCurrentMemberId();
        if (currentMemberId == null) {
            currentMemberId = 12L;
        }
        commentService.deleteComment(currentMemberId, commentId);
        return ResponseEntity.ok(GlobalResponse.ok(DELETED));
    }
}
