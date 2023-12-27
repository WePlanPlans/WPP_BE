package org.tenten.tentenbe.domain.comment.dto.response;

import java.util.List;

public record CommentResponse(
    List<CommentInfo> comments
) {
}
