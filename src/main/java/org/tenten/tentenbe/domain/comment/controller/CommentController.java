package org.tenten.tentenbe.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tenten.tentenbe.domain.comment.dto.request.CommentCreateRequest;
import org.tenten.tentenbe.domain.comment.dto.request.CommentUpdateRequest;
import org.tenten.tentenbe.domain.comment.dto.response.CommentResponse;
import org.tenten.tentenbe.domain.comment.service.CommentService;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.global.common.constant.ResponseConstant;
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.response.GlobalResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.DELETED;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Tag(name = "댓글 관련 API", description = "댓글 관련 API 모음 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성 API", description = "댓글 작성 API 입니다.")
    @ApiResponse(responseCode = "200", description = "댓글 작성 성공시", content = @Content(schema = @Schema(implementation = CommentResponse.class)))
    @PostMapping()
    public ResponseEntity<?> createComment(
        @RequestBody CommentCreateRequest commentCreateRequest
        // Security 의존성 추가될시
        // @AuthenticationPrincipal Member,
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, commentService.createComment(commentCreateRequest)));
    }

    @Operation(summary = "댓글 수정 API", description = "댓글 수정 API 입니다.")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공시", content = @Content(schema = @Schema(implementation = CommentResponse.class)))
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @Parameter(name = "commentId", description = "댓글 아이디", in = PATH)
            @PathVariable("commentId")
            Long commentId,
            @RequestBody CommentUpdateRequest commentUpdateRequest
        // Security 의존성 추가될시
        // @AuthenticationPrincipal Member,
    ) {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, commentService.updateComment(null, commentId, commentUpdateRequest)));
    }

    @Operation(summary = "댓글 삭제 API", description = "댓글 삭제 API 입니다.")
    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공시")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @Parameter(name = "commentId", description = "댓글 아이디", in = PATH)
            @PathVariable("commentId")
            Long commentId
        // Security 의존성 추가될시
        // @AuthenticationPrincipal Member,
    ) {
        commentService.deleteComment(null, commentId);
        return ResponseEntity.ok(GlobalResponse.ok(DELETED));
    }
}
