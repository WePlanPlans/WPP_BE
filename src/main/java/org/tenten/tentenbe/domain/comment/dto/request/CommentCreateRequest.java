package org.tenten.tentenbe.domain.comment.dto.request;

import org.tenten.tentenbe.domain.member.model.Member;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentCreateRequest(

	@Schema(defaultValue = "댓글 내용")
	String content
) {
}
