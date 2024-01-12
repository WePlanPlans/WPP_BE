package org.tenten.tentenbe.domain.member.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.liked.repository.LikedItemRepository;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.review.repository.ReviewRepository;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.tenten.tentenbe.domain.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.domain.fixture.MemberFixture.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikedItemRepository likedItemRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TourItemRepository tourItemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void reset() {
        entityManager.flush();
        memberRepository.deleteAll();
        likedItemRepository.deleteAll();
    }

    @Test
    @DisplayName("likedItemRepository 에서 회원을 조회할 수 있는지")
    public void myFavoriteToursSuccess() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);

        Member savedMember = memberRepository.save(newBasicMember());
        TourItem savedTourItem = tourItemRepository.save(tourItem());

        LikedItem likedItem = likedSaveItem(savedMember,savedTourItem);
        LikedItem savedLikedItem = likedItemRepository.save(likedItem);

        Page<LikedItem>  findLikedItem = likedItemRepository.findByMember(likedItem.getMember(),pageable);

        assertThat(savedLikedItem.getMember().getId()).isEqualTo(findLikedItem.get().toList().get(0).getMember().getId());
        assertThat(savedLikedItem.getMember().getEmail()).isEqualTo(findLikedItem.get().toList().get(0).getMember().getEmail());
    }

    @Test
    @DisplayName("reviewRepository 에서 리뷰와 사용자 조회를 할 수 있는지")
    public void getReviewsSuccess() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);
        Member savedMember = memberRepository.save(newBasicMember());

        reviewRepository.save(saveReview(savedMember));

        Page<Review> findReview = reviewRepository.findReviewByCreatorId(savedMember.getId(), pageable);

        assertThat(savedMember.getId()).isEqualTo(findReview.get().toList().get(0).getCreator().getId());
        assertThat(savedMember.getId()).isEqualTo(findReview.get().toList().get(0).getCreator().getId());
    }

    @Nested
    @DisplayName("memberRepository 에서")
    class memberRepository {
        @Test
        @DisplayName("회원 조회가 가능한지")
        public void findMemberSuccess() throws Exception {

            Member savedMember = memberRepository.save(newBasicMember());

            Optional<Member> findMember = memberRepository.findById(savedMember.getId());

            assertThat(savedMember.getId()).isEqualTo(findMember.get().getId());

        }

        @Test
        @DisplayName("회원 삭제가 가능한지")
        public void deleteMemberSuccess() throws Exception {

            Member savedMember = memberRepository.save(newBasicMember());

            memberRepository.delete(savedMember);

            List<Member> memberList = memberRepository.findAll();

            assertThat(memberList.size()).isEqualTo(0);

        }

    }
}
