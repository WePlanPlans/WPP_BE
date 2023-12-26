package org.tenten.tentenbe.domain.tour.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.tour.dto.response.TourDetailResponse;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.tour.repository.TourItemDetailRepository;
import org.tenten.tentenbe.domain.tour.repository.TourItemImageRepository;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourItemRepository tourItemRepository;
    private final TourItemDetailRepository tourItemDetailRepository;
    private final TourItemImageRepository tourItemImageRepository;
    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> getTours(String region) {
        return null;
    }
    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> searchTours(String region, String type, String keyword) {
        return null;
    }
    @Transactional(readOnly = true)
    public TourDetailResponse getTourDetail(Long tourId, Long memberId) {
        return null;
    }
}
