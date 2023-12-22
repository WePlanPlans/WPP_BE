package org.tenten.tentenbe.domain.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.tenten.tentenbe.domain.member.service.MemberService;
@Tag(name = "유저 관련 API", description = "유저 관련 API 모음입니다.")
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
}
