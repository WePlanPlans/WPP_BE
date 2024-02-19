package org.tenten.tentenbe.domain.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tenten.tentenbe.domain.admin.service.AdminService;
import org.tenten.tentenbe.global.response.GlobalResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Tag(name = "관리자 API", description = "관리자 API 모음 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "여행 상품 추가 API", description = "여행 상품 추가 API 입니다.")
    @PostMapping()
    public ResponseEntity<GlobalResponse> addTourItem(
        @Parameter(name = "contentTypeId", description = "여행 상품 추가할 컨텐트 타입 아이디", in = QUERY)
        @RequestParam(value = "contentTypeId")
        Long contentTypeId,
        @Parameter(name = "page", description = "페이지 번호", in = QUERY)
        @RequestParam(value = "page")
        Long page,
        @Parameter(name = "size", description = "페이지 크기", in = QUERY)
        @RequestParam(value = "size")
        Long size
    ) {
        adminService.addTourItem(contentTypeId, page, size);
        return ResponseEntity.ok(GlobalResponse.ok(SUCCESS));
    }
}
