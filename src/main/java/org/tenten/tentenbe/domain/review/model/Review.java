package org.tenten.tentenbe.domain.review.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tenten.tentenbe.domain.comment.model.Comment;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.global.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = JOINED)
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "reviewId")
    private Long id;
    private Long rating;
    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "review", fetch = LAZY, cascade = REMOVE)
    private final List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = LAZY, cascade = REMOVE)
    private final List<ReviewKeyword> reviewKeywords = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "tourItemId")
    private TourItem tourItem;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member creator;
}
