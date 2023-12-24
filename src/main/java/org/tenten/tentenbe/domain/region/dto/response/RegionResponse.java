package org.tenten.tentenbe.domain.region.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "지역 응답 객체")
public class RegionResponse {

    @Schema(name = "지역 응답 데이터 리스트")
    List<RegionInfo> regions;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(name = "지역 데이터")
    public static class RegionInfo {
        @Schema(defaultValue = "1")
        Long areaCode;
        @Schema(defaultValue = "null")
        Long subAreaCode;
        @Schema(defaultValue = "서울")
        String name;
    }
}
