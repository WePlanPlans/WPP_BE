package org.tenten.tentenbe.domain.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tenten.tentenbe.domain.category.dto.response.CategoryResponse;
import org.tenten.tentenbe.domain.category.service.CategoryService;
import org.tenten.tentenbe.global.response.GlobalDataResponse;

import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Tag(name = "카테고리 관련 API", description = "카테고리 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 조회 API", description = "카테고리 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = CategoryResponse.class)))
    @GetMapping("")
    public ResponseEntity<?> getCategory() {
        return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, categoryService.getCategory()));
    }
}
