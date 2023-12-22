package org.tenten.tentenbe.domain.comment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.global.common.BaseTimeEntity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = JOINED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "commentId")
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member creator;

    @ManyToOne
    @JoinColumn(name = "reviewId")
    private Review review;
}
