package org.tenten.tentenbe.domain.tour.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.tenten.tentenbe.common.ServiceTest;
import org.tenten.tentenbe.domain.liked.repository.LikedItemRepository;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.tour.dto.response.TourDetailResponse;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.common.fixture.TourFixture.tourSimpleResponsePage;

public class TourServiceTest extends ServiceTest {

    @InjectMocks
    private TourService tourService;
    @Mock
    private TourItemRepository tourItemRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private LikedItemRepository likedItemRepository;

    @Test
    @DisplayName("인기 여행지 조회시 페이지 리스트 값이 반환된다.")
    public void getToursSuccess(){

        Pageable pageable = PageRequest.of(0, 10);

        given(memberRepository.getReferenceById(any())).willReturn(newBasicMember());
        given(tourItemRepository.findPopularTourItems(any(), any())).willReturn(tourSimpleResponsePage());

        Page<TourSimpleResponse> result = tourService.getTours(newBasicMember().getId(),any(), pageable);

        assertNotNull(result);

        assertThat(result.getContent().get(0).title()).isEqualTo("test title");
        assertThat(result.getContent().get(0).tourAddress()).isEqualTo("test address");
        assertThat(result.getContent().get(0).longitude()).isEqualTo("longitude");
        assertThat(result.getContent().get(0).reviewCount()).isEqualTo(10L);
        assertThat(result.getContent().get(0).likedCount()).isEqualTo(20L);
    }

    @Test
    @DisplayName("지역 여행 조회시 페이지 리스트 값이 반환된다.")
    public void searchToursSuccess(){

        Pageable pageable = PageRequest.of(0, 10);

        given(memberRepository.getReferenceById(any())).willReturn(newBasicMember());
        given(tourItemRepository.searchByRegionAndCategoryAndSearchWord(any(), any(),any(),any()))
                .willReturn(new PageImpl<>(List.of(tourItem())));

        Page<TourSimpleResponse> result = tourService.searchTours(newBasicMember().getId(),"서울","관광지",any(), pageable);

        assertNotNull(result);

        assertThat(result.getContent().get(0).title()).isEqualTo("test title");
        assertThat(result.getContent().get(0).tourAddress()).isEqualTo("test address");
        assertThat(result.getContent().get(0).longitude()).isEqualTo("longitude");
        assertThat(result.getContent().get(0).reviewCount()).isEqualTo(10L);
        assertThat(result.getContent().get(0).likedCount()).isEqualTo(20L);

    }

    @Test
    @DisplayName("여행 상세 조회시 TourDetail 값이 반환된다.")
    public void searchDetailToursSuccess(){

        Pageable pageable = PageRequest.of(0, 10);

        given(memberRepository.getReferenceById(any())).willReturn(newBasicMember());
        given(tourItemRepository.findById(any())).willReturn(Optional.ofNullable(tourItem()));
        given(likedItemRepository.existsByMemberIdAndTourItemId(any(), any())).willReturn(true);

        TourDetailResponse result = tourService.getTourDetail(newBasicMember().getId(),1L);

        assertNotNull(result);

        assertThat(result).extracting(
                "title", "fullAddress", "longitude", "tel", "liked"
        ).containsExactly("test title", "test address test detail address", "longitude", "test telephone", true);

    }

}
