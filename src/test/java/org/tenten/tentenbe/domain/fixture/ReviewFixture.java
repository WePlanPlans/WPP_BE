package org.tenten.tentenbe.domain.fixture;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.tenten.tentenbe.domain.review.dto.response.ReviewInfo;
import org.tenten.tentenbe.domain.review.dto.response.ReviewResponse;
import org.tenten.tentenbe.domain.review.dto.response.TourKeywordInfo;

import java.util.List;

import static org.tenten.tentenbe.domain.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.domain.fixture.MemberFixture.review;
import static org.tenten.tentenbe.domain.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.global.common.enums.KeywordType.DINING_KEYWORD;

public class ReviewFixture {

    public static ReviewResponse reviewResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ReviewInfo> reviewInfos = List.of(ReviewInfo.fromEntity(review(), newBasicMember().getId()));
        return
                new ReviewResponse(
                        20.2,
                        tourItem().getReviewTotalCount(),
                        15L,
                        new PageImpl<>(reviewInfos, pageable, reviewInfos.size()),
                        List.of(new TourKeywordInfo(1L, "깨끗해요", DINING_KEYWORD, 2L))
                );

    }


}
