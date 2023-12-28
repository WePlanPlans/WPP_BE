package org.tenten.tentenbe.domain.tour.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TourSimpleResponse(
    @Schema(defaultValue = "1")
    Long id,

    @Schema(defaultValue = "여행지 이름")
    String title,

    @Schema(defaultValue = "4.5")
    Double ratingAverage,

    @Schema(defaultValue = "100")
    Long reviewCount,

    @Schema(defaultValue = "100")
    Long likedCount,

    @Schema(defaultValue = "false")
    Boolean liked,

    @Schema(defaultValue = "http://~~~~~~image.jpg")
    String smallThumbnailUrl
) {

}
