package org.tenten.tentenbe.domain.trip.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.tenten.tentenbe.common.RepositoryTest;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.domain.trip.dto.response.TripLikedSimpleResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse;
import org.tenten.tentenbe.domain.trip.model.Trip;
import org.tenten.tentenbe.domain.trip.model.TripLikedItem;
import org.tenten.tentenbe.domain.trip.model.TripLikedItemPreference;
import org.tenten.tentenbe.domain.trip.model.TripMember;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.common.fixture.TripFixture.*;


public class TripRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("여정 저장이 가능하다.")
    public void canSaveTripMemberRepository() throws Exception {
        Member savedmember = memberRepository.save(newBasicMember());
        Trip savedTrip = tripRepository.save(trip());

        List<TripMember> result = tripMemberRepository.saveAll(List.of(savedTripMember(savedmember,savedTrip),savedTripMember(savedmember,savedTrip)));

        assertThat(result.size()).isEqualTo(2);
    }
    @Test
    @DisplayName("회원의 총 여정 횟수 조회가 가능하다.")
    public void countTripMemberByMemberSuccess() throws Exception {
        Member savedmember = memberRepository.save(newBasicMember());
        Trip savedTrip = tripRepository.save(trip());
        tripMemberRepository.saveAll(List.of(savedTripMember(savedmember,savedTrip),savedTripMember(savedmember,savedTrip)));

        Long numberOfTrip = tripMemberRepository.countTripMemberByMember(savedmember);

        assertThat(numberOfTrip).isEqualTo(2);
    }

    @Test
    @DisplayName("나의 여정 목록 조회가 가능하다.")
    public void myTripCanSearch() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);

        Member savedmember = memberRepository.save(newBasicMember());
        Trip savedTrip = tripRepository.save(trip());
        TourItem savedTourItem = tourItemRepository.save(tourItem());

        tripItemRepository.save(savetripItem(savedTrip,savedTourItem));
        tripMemberRepository.save(savedTripMember(savedmember,savedTrip));

        Page<TripSimpleResponse> result = tripRepository.findTripsByMemberId(savedmember.getId(),pageable);

        assertThat(result.getContent().get(0).tripName()).isEqualTo("test trip name");
    }

    @Test
    @DisplayName("여정 상세 조회가 가능하다.")
    public void tripDetailSearch() throws Exception {
        Trip savedTrip = tripRepository.save(trip());

        Optional<Trip> result = tripRepository.findById(savedTrip.getId());

        assertThat(result.get().getTripName()).isEqualTo(trip().getTripName());
        assertThat(result.get().getStartDate()).isEqualTo(trip().getStartDate());

        assertThat(result.get()).isEqualTo(savedTrip);
    }

    @Test
    @DisplayName("여정 업데이트가 가능하다.")
    public void tripUpdate() throws Exception {
        Trip savedTrip = tripRepository.save(trip());

        savedTrip.updateTripInfo(tripInfoUpdateRequest());

        Trip savedUpdateTrip = tripRepository.save(savedTrip);

        assertThat(savedTrip.getTripName()).isEqualTo(savedUpdateTrip.getTripName());
        assertThat(savedTrip.getStartDate()).isEqualTo(savedUpdateTrip.getStartDate());

        assertThat(savedTrip).isEqualTo(savedUpdateTrip);

    }
    @Test
    @DisplayName("그룹 관심 여행지 등록이 가능하다.")
    public void likeTourInOutTrip() throws Exception {

        Trip savedTrip = tripRepository.save(trip());
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        TripLikedItem savedTripLikedItem = tripLikedItemRepository.save(saveTripLikedItem(savedTrip,savedTourItem));

        assertThat(savedTripLikedItem.getTrip()).usingRecursiveComparison().isEqualTo(savedTrip);
        assertThat(savedTripLikedItem.getTourItem()).usingRecursiveComparison().isEqualTo(tourItem());

    }


    @Test
    @DisplayName("그룹 관심 목록 조회가 가능하다.")
    public void getTripLikedItems() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);
        Member savedMember = memberRepository.save(newBasicMember());
        Trip savedTrip = tripRepository.save(trip());
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        TripLikedItem savedTripLikedItem = tripLikedItemRepository.save(saveTripLikedItem(savedTrip,savedTourItem));

        Page<TripLikedSimpleResponse> result = tripLikedItemRepository
                .findTripLikedItemsById(savedMember.getId(), savedTrip.getId(), 12L, pageable);


        //TODO:: 관심 그룹 목록 조회가 완료후 재 테스트

        assertThat(savedTripLikedItem).isEqualTo(result.getContent().get(0));

    }

    @Test
    @DisplayName("그룹 관심 여행지 좋아요 /싫어요 저장 가능하다. ")
    public void preferOrNotTourInOurTrip() throws Exception {

        Member savedMember = memberRepository.save(newBasicMember());
        Trip savedTrip = tripRepository.save(trip());
        TourItem savedTourItem = tourItemRepository.save(tourItem());
        TripMember savedTripMember = tripMemberRepository.save(savedTripMember(savedMember, savedTrip));

        TripLikedItem savedTripLikedItem = tripLikedItemRepository.save(saveTripLikedItem(savedTrip,savedTourItem));

        TripLikedItemPreference savedTripLikedItemPreference =
                tripLikedItemPreferenceRepository.save(saveTripLikedItemPreference(savedTripMember, savedTripLikedItem));

        Optional<TripLikedItemPreference> result = tripLikedItemPreferenceRepository.findById(savedTripLikedItemPreference.getId());

        assertThat(result.get()).usingRecursiveComparison().isEqualTo(savedTripLikedItemPreference);
    }
}
