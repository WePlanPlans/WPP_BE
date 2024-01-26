package org.tenten.tentenbe.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentCreateRequest(

    @Schema(defaultValue = "여기 조식 어떤가요?")
    String content,
    @Schema(defaultValue = "1")
    Long reviewId
) {
}
