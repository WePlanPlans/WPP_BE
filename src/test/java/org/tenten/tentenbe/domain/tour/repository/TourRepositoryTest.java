package org.tenten.tentenbe.domain.tour.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.tenten.tentenbe.common.RepositoryTest;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.tour.model.TourItem;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.AuthFixture.updateMember;
import static org.tenten.tentenbe.common.fixture.MemberFixture.*;

public class TourRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("회원 조회가 가능하다")
    public void findMemberSuccess() throws Exception {
        Member savedMember = memberRepository.save(newBasicMember());
        Optional<Member> findMember = memberRepository.findById(savedMember.getId());

        assertThat(savedMember.getId()).isEqualTo(findMember.get().getId());
    }

    @Nested
    @DisplayName("tourItemsRepository 에서")
    class tourItemRepository {
        @Test
        @DisplayName("인기 여행 아이템 조회가 가능하다. ")
        public void findById() throws Exception {

            Pageable pageable = PageRequest.of(0, 10);

            tourItemRepository.save(tourItem());
            Page<TourSimpleResponse> findPopularTourItem = tourItemRepository.findPopularTourItems(newBasicMember().getId(), pageable);

            assertThat(findPopularTourItem.getContent().get(0).likedCount()).isEqualTo(20L);
            assertThat(findPopularTourItem.getContent().get(0).tourAddress()).isEqualTo("test address");
            assertThat(findPopularTourItem.getContent().get(0).reviewCount()).isEqualTo(10L);
            assertThat(findPopularTourItem.getContent().get(0).smallThumbnailUrl()).isEqualTo("small thumnail url");

        }

        @Test
        @DisplayName("지역 카테고리 필터 조회가 가능하다.")
        public void findPopularTourItems() throws Exception {

            Pageable pageable = PageRequest.of(0, 10);

            Long regionCode = 1L;
            Long categoryCode = 12L;
            String searchWord = "search address";

            tourItemRepository.saveAll(List.of(tourItem(),tourItemSecond()));

            Page<TourItem> searchTourItem = tourItemRepository.searchByRegionAndCategoryAndSearchWord(regionCode, categoryCode, searchWord, pageable);

            assertThat(searchTourItem.getContent().size()).isEqualTo(1);
            assertThat(searchTourItem.getContent().get(0).getContentId()).isEqualTo(1L);
            assertThat(searchTourItem.getContent().get(0).getContentTypeId()).isEqualTo(12L);
            assertThat(searchTourItem.getContent().get(0).getAreaCode()).isEqualTo(1L);
            assertThat(searchTourItem.getContent().get(0).getAddress()).isEqualTo("search address");
        }
    }

    @Nested
    @DisplayName("likedItemRepository 에서")
    class likedItemRepository {
        @Test
        @DisplayName("해당 유저가 여행 상품에 좋아요를 '누른지' 확인 가능하다.")
        public void existsTourItemSuccess() throws Exception {

            Member savedMember = memberRepository.save(newBasicMember());
            TourItem savedTourItem = tourItemRepository.save(tourItem());

            LikedItem likedItem = likedSaveItem(savedMember,savedTourItem);
            likedItemRepository.save(likedItem);

            Boolean result = likedItemRepository.existsByMemberIdAndTourItemId(savedMember.getId(), savedTourItem.getId());

            assertThat(result).isEqualTo(true);

        }

        @Test
        @DisplayName("해당 유저가 여행 상품에 좋아요를 누르지 '않은지' 확인 가능하다.")
        public void existsTourItemFail() throws Exception {

            Member savedMember = memberRepository.save(newBasicMember());
            Member savedOtherMember = memberRepository.save(updateMember());

            TourItem savedTourItem = tourItemRepository.save(tourItem());

            LikedItem likedItem = likedSaveItem(savedMember,savedTourItem);
            likedItemRepository.save(likedItem);

            Boolean result = likedItemRepository.existsByMemberIdAndTourItemId(savedOtherMember.getId(), savedTourItem.getId());

            assertThat(result).isEqualTo(false);

        }
    }
}
