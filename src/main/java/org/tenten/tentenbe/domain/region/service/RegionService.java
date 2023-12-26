package org.tenten.tentenbe.domain.region.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tenten.tentenbe.domain.region.dto.response.RegionResponse;
import org.tenten.tentenbe.global.common.enums.Region;
import org.tenten.tentenbe.global.component.OpenApiComponent;

@Service
@RequiredArgsConstructor
public class RegionService {
    private final OpenApiComponent openApiComponent;

    public RegionResponse getRegions(String areaCode) {
        if (areaCode == null) {
            return new RegionResponse(
                    Region.entireRegions.stream().map(region -> new RegionResponse.RegionInfo(region.getAreaCode(), region.getSubAreaCode(), region.getName())).toList());
        } else {
            return new RegionResponse(
                    openApiComponent.getSubRegion(areaCode)
            );
        }
    }

    public RegionResponse getPopularRegions() {
        return new RegionResponse(
                Region.popularPlaces.stream().map(region -> new RegionResponse.RegionInfo(region.getAreaCode(), region.getSubAreaCode(), region.getName())).toList());

    }
}
