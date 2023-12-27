package org.tenten.tentenbe.domain.region.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record RegionResponse(List<RegionInfo> regions) {
    public record RegionInfo(
        @Schema(defaultValue = "1")
        Long areaCode,
        @Schema(defaultValue = "null")
        Long subAreaCode,
        @Schema(defaultValue = "서울시")
        String name
    ) {

    }
}
