package org.tenten.tentenbe.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CommentInfo(
    @Schema(defaultValue = "1")
    Long contentId,
    @Schema(defaultValue = "은별")
    String authorNickname,
    @Schema(defaultValue = "https://~~~.png")
    String authorProfileImageUrl,
    @Schema(defaultValue = "잘보고 갑니다~")
    String content,
    @Schema(defaultValue = "2023-12-26T12:00:00")
    LocalDateTime createdTime) {
}
