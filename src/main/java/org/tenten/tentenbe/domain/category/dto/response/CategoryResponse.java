package org.tenten.tentenbe.domain.category.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "카테고리 응답 객체")
public class CategoryResponse {
    @Schema(defaultValue = "39")
    Long code;
    @Schema(defaultValue = "식당")
    String name;
}
