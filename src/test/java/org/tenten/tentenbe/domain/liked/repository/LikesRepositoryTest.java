package org.tenten.tentenbe.domain.liked.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.tenten.tentenbe.common.RepositoryTest;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.MemberFixture.likedSaveItem;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.common.fixture.ReviewFixture.saveReview;

public class LikesRepositoryTest extends RepositoryTest {

    @Autowired
    LikedItemRepository likedItemRepository;

    @Test
    @DisplayName("좋아요 추가가 가능하다.")
    public void testTourItemRepository() {
        //given
        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));
        LikedItem savedLikedItem = likedItemRepository.save(likedSaveItem(savedMember,savedTourItem));

        //when
        LikedItem likedItem = likedItemRepository.save(savedLikedItem);
        //then
        assertThat(likedItem.getMember()).isEqualTo(savedMember);
        assertThat(likedItem.getTourItem()).isEqualTo(savedTourItem);
    }

    @Test
    @DisplayName("좋아요 삭제가 가능하다.")
    public void testLikedItemRepository() {
        //given
        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        Review savedReview = reviewRepository.save(saveReview(savedTourItem,savedMember));
        LikedItem savedLikedItem = likedItemRepository.save(likedSaveItem(savedMember,savedTourItem));

        //when
        LikedItem likedItem = likedItemRepository.save(savedLikedItem);
        assertThat(likedItemRepository.findAll().size()).isEqualTo(1);
        //then
        likedItemRepository.deleteById(likedItem.getId());
        assertThat(likedItemRepository.findAll().size()).isEqualTo(0);

    }
}