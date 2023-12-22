package org.tenten.tentenbe.domain.tour.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tenten.tentenbe.domain.tour.repository.TourItemDetailRepository;
import org.tenten.tentenbe.domain.tour.repository.TourItemImageRepository;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourItemRepository tourItemRepository;
    private final TourItemDetailRepository tourItemDetailRepository;
    private final TourItemImageRepository tourItemImageRepository;
}
