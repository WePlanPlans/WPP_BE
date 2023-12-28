package org.tenten.tentenbe.domain.tour.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.global.common.enums.KeywordType;

import java.time.LocalDateTime;
import java.util.List;

public record TourReviewSimpleInfo(
    @Schema(defaultValue = "1")
    Long reviewId,
    @Schema(defaultValue = "은별")
    String authorNickname,
    @Schema(defaultValue = "https://~~~.png")
    String authorProfileImageUrl,
    @Schema(defaultValue = "4")
    Long rating,
    @Schema(defaultValue = "2023-12-26T12:00:00")
    LocalDateTime createdTime,
    @Schema(defaultValue = "~~~여서 ~~~ 해서 ~~로 좋았습니다.")
    String content,
    List<TourKeywordSimpleInfo> tourKeywordSimpleInfos
) {
}
