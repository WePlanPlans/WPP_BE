package org.tenten.tentenbe.domain.review.dto.response;

import java.util.List;

public record ReviewResponse(
        List<ReviewInfo> reviewInfos
) {

}
