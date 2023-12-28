package org.tenten.tentenbe.domain.tour.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.domain.tour.model.TourItem;

public record TourDetailResponse(
    @Schema(defaultValue = "1")
    Long id,

    @Schema(defaultValue = "여행지 이름")
    String title,

    @Schema(defaultValue = "false")
    Boolean liked,

    @Schema(defaultValue = "OO시/도 OO구/군 OO로/길 OOO")
    String fullAddress,

    @Schema(defaultValue = "00000")
    String zipcode,

    @Schema(defaultValue = "127.04")
    String longitude,

    @Schema(defaultValue = "33.56")
    String latitude,

    @Schema(defaultValue = "010-0000-0000")
    String tel,

    @Schema(defaultValue = "http://~~~~~~image.jpg")
    String originalThumbnailUrl
) {
    public TourDetailResponse(
        TourItem tourItem,
        Boolean liked
    ) {
        this(
            tourItem.getId(),
            tourItem.getTitle(),
            liked,
            tourItem.getAddress()+tourItem.getDetailedAddress(),
            tourItem.getZipcode(),
            tourItem.getLongitude(),
            tourItem.getLatitude(),
            tourItem.getTel(),
            tourItem.getOriginalThumbnailUrl()
        );
    }
}
