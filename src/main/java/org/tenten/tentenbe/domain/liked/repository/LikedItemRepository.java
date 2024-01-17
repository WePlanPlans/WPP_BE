package org.tenten.tentenbe.domain.liked.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import java.util.Optional;

public interface LikedItemRepository extends JpaRepository<LikedItem, Long> {
    Optional<LikedItem> findByMemberAndTourItem(Member member, TourItem tourItem);

    Page<LikedItem> findByMember(Member member, Pageable pageable);

    Page<LikedItem> findByMemberAndTourItem_ContentTypeId(Member member, Long categoryCode, Pageable pageable);

    boolean existsByMemberIdAndTourItemId(Long memberId, Long tourItemId);
}
