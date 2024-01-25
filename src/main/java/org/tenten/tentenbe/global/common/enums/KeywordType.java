package org.tenten.tentenbe.global.common.enums;

import lombok.Getter;
import org.tenten.tentenbe.domain.review.exception.KeywordException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
public enum KeywordType {
    DINING_KEYWORD("DINING", 39L),
    ACCOMMODATION_KEYWORD("ACCOMMODATION", 32L),
    ATTRACTION_KEYWORD("ATTRACTION", 12L);

    private final String name;
    private final Long code;

    KeywordType(String name, Long code) {
        this.name = name;
        this.code = code;
    }

    public static KeywordType fromName(String name) {
        for (KeywordType keywordType : KeywordType.values()) {
            if (keywordType.getName().equals(name)) {
                return keywordType;
            }
        }
        throw new KeywordException("주어진 이름으로 존재하는 키워드가 없습니다." + name, NOT_FOUND);
    }
}
