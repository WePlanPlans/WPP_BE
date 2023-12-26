package org.tenten.tentenbe.domain.review.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tenten.tentenbe.domain.review.model.ReviewKeyword;
import org.tenten.tentenbe.global.common.enums.KeywordType;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KeyWord {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "keyWordId")
    private Long id;
    private String content;
    @Enumerated(STRING)
    private KeywordType type;

    @OneToMany(mappedBy = "keyWord", fetch = LAZY, cascade = REMOVE)
    private final List<ReviewKeyword> reviewKeywords = new ArrayList<>();
}
