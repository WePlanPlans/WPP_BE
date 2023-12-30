package org.tenten.tentenbe.domain.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import java.util.List;

public record ReviewCreateRequest(
    @Schema(defaultValue = "1")
    Long tourItemId,
    @Schema(defaultValue = "3")
    Long rating,
    List<ReviewKeywordCreateRequest> keywords,
    @Schema(defaultValue = "너무 맘에 드는 여행지였습니다.")
    String content
) {
    public Review toEntity(Member member) {

        return Review.builder()
            .creator(member)
            .content(content)
            .tourItem(TourItem.builder().id(tourItemId).build())
            .rating(rating)
            .build();
    }
}
