package org.tenten.tentenbe.domain.tour.dto.response;

import java.util.List;

public record TourSimpleResponsePage(
    List<TourSimpleResponse> tourSimpleResponseList,
    long totalElements
) {
}
