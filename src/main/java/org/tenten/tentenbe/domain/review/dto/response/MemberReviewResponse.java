package org.tenten.tentenbe.domain.review.dto.response;

import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.review.model.Keyword;
import org.tenten.tentenbe.domain.review.model.Review;

import java.time.LocalDateTime;
import java.util.List;

public record MemberReviewResponse(
    Long reviewId,
    String authorNickname,
    String authorProfileImageUrl,
    Long rating,
    LocalDateTime createdTime,
    String content,
    List<KeywordInfo> keywords,
    Long commentCount
) {
    public static MemberReviewResponse fromEntity(Review review) {
        Member creator = review.getCreator();
        return new MemberReviewResponse(
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
}
