package org.tenten.tentenbe.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentUpdateRequest(

    @Schema(defaultValue = "후식 어떤가요?")
    String content
) {
}
