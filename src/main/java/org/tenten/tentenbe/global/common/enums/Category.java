package org.tenten.tentenbe.global.common.enums;

import lombok.Getter;

@Getter
public enum Category {
    DINING(39L), ACCOMMODATION(32L), ATTRACTION(12L);

    private final Long id;

    Category(Long id) {
        this.id = id;
    }
}
