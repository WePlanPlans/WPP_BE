package org.tenten.tentenbe.domain.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import java.util.List;

public interface TourItemRepository extends JpaRepository<TourItem, Long> {
    List<TourItem> findByAreaCode(Long areaCode);
}
