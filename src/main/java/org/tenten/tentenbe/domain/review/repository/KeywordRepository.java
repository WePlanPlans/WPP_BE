package org.tenten.tentenbe.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.review.model.KeyWord;

public interface KeywordRepository extends JpaRepository<KeyWord, Long> {
}
