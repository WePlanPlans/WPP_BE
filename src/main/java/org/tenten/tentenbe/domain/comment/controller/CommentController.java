package org.tenten.tentenbe.domain.comment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.tenten.tentenbe.domain.comment.service.CommentService;
@Tag(name = "댓글 관련 API", description = "댓글 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
}
