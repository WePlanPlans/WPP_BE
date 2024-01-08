package org.tenten.tentenbe.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.review.model.Keyword;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.review.model.ReviewKeyword;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewInfo(
    @Schema(defaultValue = "1")
    Long reviewId,
    @Schema(defaultValue = "은별")
    String authorNickname,
    @Schema(defaultValue = "https://~~~.png")
    String authorProfileImageUrl,
    @Schema(defaultValue = "4.5")
    Double rating,
    @Schema(defaultValue = "2023-12-26T12:00:00")
    LocalDateTime createdTime,
    @Schema(defaultValue = "~~~여서 ~~~ 해서 ~~로 좋았습니다.")
    String content,
    List<KeywordInfo> keywords,
    Long commentCount) {

    public static ReviewInfo fromEntity(Review review) {
        Member creator = review.getCreator();
        return new ReviewInfo(
            review.getId(),
            creator.getNickname(),
            creator.getProfileImageUrl(),
            review.getRating(),
            review.getCreatedTime(),
            review.getContent(),
            review.getReviewKeywords().stream().map(reviewKeyword -> {
                Keyword keyword = reviewKeyword.getKeyword();
                return new KeywordInfo(keyword.getId(), keyword.getContent(), keyword.getType());
            }).toList(),
            (long) review.getComments().size()
        );
    }

    public static ReviewInfo fromEntity(Review review, List<ReviewKeyword> reviewKeywords) {
        Member creator = review.getCreator();
        return new ReviewInfo(
            review.getId(),
            creator.getNickname(),
            creator.getProfileImageUrl(),
            review.getRating(),
            review.getCreatedTime(),
            review.getContent(),
            reviewKeywords.stream().map(reviewKeyword -> {
                Keyword keyword = reviewKeyword.getKeyword();
                return new KeywordInfo(keyword.getId(), keyword.getContent(), keyword.getType());
            }).toList(),
            (long) review.getComments().size()
        );
    }

}
