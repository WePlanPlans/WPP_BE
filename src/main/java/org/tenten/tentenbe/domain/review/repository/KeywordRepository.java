package org.tenten.tentenbe.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tenten.tentenbe.domain.review.dto.response.TourKeywordInfo;
import org.tenten.tentenbe.domain.review.model.Keyword;
import org.tenten.tentenbe.global.common.enums.KeywordType;

import java.security.Key;
import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    @Query("SELECT NEW org.tenten.tentenbe.domain.review.dto.response.TourKeywordInfo(" +
        "kw.id," +
        "kw.content, " +
        "kw.type, " +
        "CAST(COALESCE(COUNT(kw.id),0 ) AS LONG) " +
        ") FROM Keyword kw " +
        "LEFT OUTER JOIN ReviewKeyword rk ON kw.id = rk.keyword.id " +
        "LEFT OUTER JOIN Review r ON rk.review.id = r.id " +
        "LEFT OUTER JOIN TourItem ti ON r.tourItem.id = ti.id " +
        "WHERE ti.id IS NULL OR ti.id = :tourItemId GROUP BY kw.id")
    List<TourKeywordInfo> findKeywordInfoByTourItemId(@Param("tourItemId") Long tourItemId);

    List<Keyword> findByType(KeywordType keywordType);
}
