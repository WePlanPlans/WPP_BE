package org.tenten.tentenbe.domain.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.tour.model.TourItemDetail;

public interface TourItemDetailRepository extends JpaRepository<TourItemDetail, Long> {
}
