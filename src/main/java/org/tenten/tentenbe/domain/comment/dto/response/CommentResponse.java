package org.tenten.tentenbe.domain.comment.dto.response;

import org.springframework.data.domain.Page;

public record CommentResponse(
    Page<CommentInfo> comments
) {
}
