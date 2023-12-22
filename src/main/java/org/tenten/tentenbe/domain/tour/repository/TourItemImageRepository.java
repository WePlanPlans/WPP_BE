package org.tenten.tentenbe.domain.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.tour.model.TourItemImage;

public interface TourItemImageRepository extends JpaRepository<TourItemImage, Long> {
}
