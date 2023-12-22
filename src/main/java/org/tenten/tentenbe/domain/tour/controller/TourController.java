package org.tenten.tentenbe.domain.tour.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.tenten.tentenbe.domain.tour.service.TourService;
@Tag(name = "여행지 관련 API", description = "여행지 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;
}
