package org.tenten.tentenbe.domain.comment.dto.response;

import org.tenten.tentenbe.domain.member.model.Member;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentResponse(

	@Schema(defaultValue = "댓글 내용")
	String content,
	@Schema(defaultValue = "사용자 이름")
	String userName
) {
}
