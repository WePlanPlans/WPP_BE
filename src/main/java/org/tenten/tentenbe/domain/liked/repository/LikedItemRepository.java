package org.tenten.tentenbe.domain.liked.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import java.util.Optional;

public interface LikedItemRepository extends JpaRepository<LikedItem, Long> {
    Optional<LikedItem> findByMemberAndTourItem(Member member, TourItem tourItem);
}
