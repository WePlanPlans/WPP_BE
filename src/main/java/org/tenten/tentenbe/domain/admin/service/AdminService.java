package org.tenten.tentenbe.domain.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;
import org.tenten.tentenbe.global.component.OpenApiComponent;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final TourItemRepository tourItemRepository;
    private final OpenApiComponent openApiComponent;
    @Transactional
    public void addTourItem(Long contentTypeId, Long page, Long size) {
        List<TourItem> tourItems = openApiComponent.getTourItems(contentTypeId, page, size);
        tourItemRepository.saveAll(tourItems);
    }
}
