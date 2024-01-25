package org.tenten.tentenbe.domain.comment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.global.common.BaseTimeEntity;

import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Getter
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


    public Comment(String content, Review review) {
        this.content = content;
        this.review = review;
    }

    public void UpdateComment(String content) {
        this.content = content;
    }

    public void addReview(Review review) {
        this.review = review;
        if (!review.getComments().contains(this)) review.getComments().add(this);
    }

    public void removeReview() {
        if (this.review != null) {
            review.getComments().remove(this);
            this.review = null;
        }
    }

    public void addCreator(Member creator) {
        this.creator = creator;
        if (!creator.getComments().contains(this)) creator.getComments().add(this);
    }

    public void removeCreator() {
        if (this.creator != null) {
            creator.getComments().remove(this);
            this.creator = null;
        }
    }
}
