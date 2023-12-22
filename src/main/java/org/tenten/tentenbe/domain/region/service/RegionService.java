package org.tenten.tentenbe.domain.region.service;

import org.springframework.stereotype.Service;
import org.tenten.tentenbe.domain.region.dto.response.RegionResponse;
import org.tenten.tentenbe.global.common.enums.Region;

import java.util.List;

@Service
public class RegionService {
    public List<RegionResponse> getRegions() {
        return Region.entireRegions.stream().map(region -> {
            return new RegionResponse(region.getAreaCode(), region.getSubAreaCode(), region.getName());
        }).toList();
    }

    public List<RegionResponse> getPopularRegions() {
        return Region.popularPlaces.stream().map(region -> {
            return new RegionResponse(region.getAreaCode(), region.getSubAreaCode(), region.getName());
        }).toList();

    }
}
