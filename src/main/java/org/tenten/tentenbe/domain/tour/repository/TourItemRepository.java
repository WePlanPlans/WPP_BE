package org.tenten.tentenbe.domain.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import java.util.List;

public interface TourItemRepository extends JpaRepository<TourItem, Long>, JpaSpecificationExecutor<TourItem> {
    List<TourItem> findByAreaCode(Long areaCode);
}
