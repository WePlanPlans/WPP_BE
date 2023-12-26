package org.tenten.tentenbe.domain.comment.dto.response;

import org.tenten.tentenbe.domain.member.model.Member;

public record CommentResponse(
	String content,
	Member creator
) {
}
