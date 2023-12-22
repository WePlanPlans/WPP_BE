package org.tenten.tentenbe.domain.review.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.tenten.tentenbe.domain.review.service.ReviewService;
@Tag(name = "리뷰 관련 API", description = "리뷰 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
}
