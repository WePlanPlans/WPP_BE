package org.tenten.tentenbe.common;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.tenten.tentenbe.domain.liked.repository.LikedItemRepository;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.review.repository.ReviewRepository;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoryTest {

    @Autowired
    protected TourItemRepository tourItemRepository;
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected LikedItemRepository likedItemRepository;
    @Autowired
    protected ReviewRepository reviewRepository;
    @PersistenceContext
    protected EntityManager entityManager;
    @BeforeEach
    protected void init() {
        entityManager.flush();
        System.out.println("flushed data");
        tourItemRepository.deleteAll();
        memberRepository.deleteAll();
        likedItemRepository.deleteAll();
        reviewRepository.deleteAll();
        System.out.println("reset success");
    }

}
