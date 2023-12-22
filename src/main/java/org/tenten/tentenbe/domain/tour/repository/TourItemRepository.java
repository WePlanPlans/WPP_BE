package org.tenten.tentenbe.domain.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.tour.model.TourItem;

public interface TourItemRepository extends JpaRepository<TourItem, Long> {
}
