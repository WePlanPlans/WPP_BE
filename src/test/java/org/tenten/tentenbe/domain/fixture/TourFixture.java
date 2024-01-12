package org.tenten.tentenbe.domain.fixture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import java.util.List;

import static org.tenten.tentenbe.domain.fixture.MemberFixture.tourItem;

public class TourFixture {
    public static Page<TourSimpleResponse> tourSimpleResponsePage() {
        List<TourSimpleResponse> tourSimpleResponse = List.of(TourSimpleResponse.fromEntity(tourItem()));
        Pageable pageable = PageRequest.of(0, 10);
       return new PageImpl<>(tourSimpleResponse, pageable, tourSimpleResponse.size());
    }
}
