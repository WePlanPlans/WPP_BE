package org.tenten.tentenbe.domain.trip.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.tenten.tentenbe.common.ServiceTest;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;
import org.tenten.tentenbe.domain.trip.dto.response.*;
import org.tenten.tentenbe.domain.trip.model.TripLikedItem;
import org.tenten.tentenbe.domain.trip.model.TripLikedItemPreference;
import org.tenten.tentenbe.domain.trip.repository.TripLikedItemPreferenceRepository;
import org.tenten.tentenbe.domain.trip.repository.TripLikedItemRepository;
import org.tenten.tentenbe.domain.trip.repository.TripMemberRepository;
import org.tenten.tentenbe.domain.trip.repository.TripRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.common.fixture.TripFixture.*;

public class TripServiceTest extends ServiceTest {

    @InjectMocks
    private TripService tripService;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private TourItemRepository tourItemRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private  TripMemberRepository tripMemberRepository;
    @Mock
    private  TripLikedItemRepository tripLikedItemRepository;
    @Mock
    private  TripLikedItemPreferenceRepository tripLikedItemPreferenceRepository;

    @Test
    @DisplayName("여정 생성이 가능하다.")
    public void createTripSuccess(){

        given(tripMemberRepository.countTripMemberByMember(any())).willReturn(1L);
        given(tripMemberRepository.save(any())).willReturn(savedTripMember(newBasicMember(),trip()));
        given(tripRepository.save(any())).willReturn(trip());

        TripCreateResponse result = tripService.createTrip(newBasicMember().getId(), tripCreateRequest());

        assertNotNull(result);
        assertThat(result.tripId()).isEqualTo(10L);
    }

    @Test
    @DisplayName("여정 조회가 가능하다.")
    public void getTripsSuccess(){

        Pageable pageable = PageRequest.of(0, 10);

        List<TripSimpleResponse> tripSimpleResponseList = List.of(tripSimpleResponse());
        Page<TripSimpleResponse> tripSimpleResponsePage = new PageImpl<>(tripSimpleResponseList, pageable, tripSimpleResponseList.size());

        given(tripRepository.findTripsByMemberId(any(),any())).willReturn(tripSimpleResponsePage);

        Page<TripSimpleResponse> result = tripService.getTrips(newBasicMember().getId(), pageable);

        assertNotNull(result);

        assertThat(result.getContent()).usingRecursiveAssertion().isEqualTo(tripSimpleResponsePage.getContent());
    }

    @Test
    @DisplayName("상세 여정 조회가 가능하다.")
    public void getTripSuccess(){

        given(tripRepository.findById(any())).willReturn(Optional.ofNullable(trip()));

        TripDetailResponse result = tripService.getTrip(trip().getId());

        assertNotNull(result);
        assertThat(result.tripName()).isEqualTo(trip().getTripName());
        assertThat(result.startDate()).isEqualTo(trip().getStartDate());
        assertThat(result.endDate()).isEqualTo(trip().getEndDate());
    }

    @Test
    @DisplayName("여정 업데이트가 가능하다.")
    public void updateTripSuccess(){

        given(memberRepository.getReferenceById(any())).willReturn(newBasicMember());
        given(tripRepository.findById(any())).willReturn(Optional.ofNullable(trip()));
        given(tripRepository.save(any())).willReturn(trip());

        TripInfoUpdateResponse result = tripService.updateTrip(newBasicMember().getId(), trip().getId(),tripInfoUpdateRequest());

        assertNotNull(result);
        assertThat(result.tripName()).isEqualTo(tripInfoUpdateRequest().tripName());
    }

    @Test
    @DisplayName("여정 멤버 삭제가 가능하다.")
    public void deleteTripMemberSuccess(){
        //TODO:: 구현 구상 후 작성 예정
    }

    @Test
    @DisplayName("그룹 관심 여행지 등록 이 가능하다.")
    public void LikeTourInOurTripSuccess(){

        given(memberRepository.getReferenceById(any())).willReturn(newBasicMember());
        given(tripRepository.findById(any())).willReturn(Optional.ofNullable(trip()));
        given(tourItemRepository.findById(any())).willReturn(Optional.ofNullable(tourItem()));
        given(tripLikedItemRepository.findByTripAndTourItem(any(),any()))
                .willReturn(Optional.empty());

        tripService.LikeTourInOurTrip(newBasicMember().getId(), trip().getId(), tripLikedItemRequest());

        verify(tripLikedItemRepository, times(4)).save(any(TripLikedItem.class));

    }

    @Test
    @DisplayName("그룹 관심 목록 조회가 가능하다. ")
    public void getTripLikedItemsSuccess(){

        Pageable pageable = PageRequest.of(0, 10);

        List<TripLikedItem> tripLikedItems = List.of(saveTripLikedItem(trip(),tourItem()));
        Page<TripLikedItem> tripLikedItemPage = new PageImpl<>(tripLikedItems, pageable, tripLikedItems.size());


        given(memberRepository.getReferenceById(any())).willReturn(newBasicMember());
        given(tripRepository.findById(any())).willReturn(Optional.ofNullable(trip()));
        given(tripLikedItemRepository.findTripLikedItemsById(any(), any(), any(), any()))
                .willReturn(new PageImpl<>(List.of(tripLikedSimpleResponse()), pageable, tripLikedItems.size()));

        Page<TripLikedSimpleResponse> result = tripService.getTripLikedItems(newBasicMember().getId(), trip().getId(), "관광지", pageable);

        assertNotNull(result);

        assertThat(result.getContent()).usingRecursiveComparison()
                .isEqualTo(List.of(tripLikedSimpleResponse()));

    }

    @Test
    @DisplayName("그룹 관심 여행지 좋아요 여부 확인 이 가능하다.")
    public void preferOrNotTourInOurTripSuccess(){

        given(tripRepository.findById(any())).willReturn(Optional.ofNullable(trip()));
        given(tourItemRepository.findById(any())).willReturn(Optional.ofNullable(tourItem()));
        given(tripMemberRepository.findByMemberAndTrip(any(),any())).willReturn(Optional.ofNullable(savedTripMember(newBasicMember(),trip())));
        given(tripLikedItemRepository.findByTripAndTourItem(any(), any()))
                .willReturn(Optional.ofNullable(saveTripLikedItem(trip(), tourItem())));
//        given(tripLikedItemPreferenceRepository.findByTripMemberAndTripLikedItem(savedTripMember(), saveTripLikedItem(trip(), tourItem())))
//                .willReturn(Optional.ofNullable(tripLikedItemPreference()));

        tripService.preferOrNotTourInOurTrip(newBasicMember().getId(), trip().getId(), tourItem().getId(), false, true);

        verify(tripLikedItemPreferenceRepository).save(any(TripLikedItemPreference.class));

    }

    @Test
    @DisplayName("그룹 여행 취향 조회가 가능하다.")
    public void getTripSurveysSuccess(){

        //TODO:: 코드 보완필
        given(tripRepository.findById(any())).willReturn(Optional.ofNullable(trip()));
//        given(tripMemberRepository.findByMemberAndTrip(any(),any())).willReturn(Optional.ofNullable(savedTripMember()));

        TripSurveyResponse result = tripService.getTripSurveys(trip().getId());

        assertThat(result.planningTotalCount()).isEqualTo(0);

    }
}
