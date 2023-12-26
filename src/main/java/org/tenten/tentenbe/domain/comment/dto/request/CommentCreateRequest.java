package org.tenten.tentenbe.domain.comment.dto.request;

import org.tenten.tentenbe.domain.member.model.Member;

public record CommentCreateRequest(
	String content
) {
}
