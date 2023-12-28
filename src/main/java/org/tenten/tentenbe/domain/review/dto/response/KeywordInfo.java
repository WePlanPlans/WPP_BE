package org.tenten.tentenbe.domain.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tenten.tentenbe.domain.review.model.Keyword;
import org.tenten.tentenbe.global.common.enums.KeywordType;

public record KeywordInfo(
    @Schema(defaultValue = "1")
    Long keywordId,
    @Schema(defaultValue = "깨끗해요")
    String content,
    @Schema(defaultValue = "ACCOMMODATION_KEYWORD")
    KeywordType type) {
    public static KeywordInfo fromEntity(Keyword keyword) {
        return new KeywordInfo(keyword.getId(), keyword.getContent(), keyword.getType());
    }
}
