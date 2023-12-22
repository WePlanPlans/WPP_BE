package org.tenten.tentenbe.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.member.model.LikedItem;

public interface LikedItemRepository extends JpaRepository<LikedItem, Long> {
}
