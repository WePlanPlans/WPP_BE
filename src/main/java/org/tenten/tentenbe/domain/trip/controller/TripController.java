package org.tenten.tentenbe.domain.trip.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.tenten.tentenbe.domain.trip.service.TripService;
@Tag(name = "여정 관련 API", description = "여정 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;
}
