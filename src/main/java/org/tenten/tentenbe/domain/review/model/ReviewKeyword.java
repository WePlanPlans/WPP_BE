package org.tenten.tentenbe.domain.review.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewKeyword {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reviewTagId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reviewId")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "keyWordId")
    private KeyWord keyWord;
}
