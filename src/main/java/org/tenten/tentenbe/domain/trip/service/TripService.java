package org.tenten.tentenbe.domain.trip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.member.exception.MemberException;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.tour.exception.TourException;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;
import org.tenten.tentenbe.domain.trip.dto.request.TripCreateRequest;
import org.tenten.tentenbe.domain.trip.dto.request.TripInfoUpdateRequest;
import org.tenten.tentenbe.domain.trip.dto.request.TripLikedItemRequest;
import org.tenten.tentenbe.domain.trip.dto.response.*;
import org.tenten.tentenbe.domain.trip.exception.TripException;
import org.tenten.tentenbe.domain.trip.exception.TripLikeItemPreferenceException;
import org.tenten.tentenbe.domain.trip.exception.TripLikedItemException;
import org.tenten.tentenbe.domain.trip.exception.TripMemberException;
import org.tenten.tentenbe.domain.trip.model.Trip;
import org.tenten.tentenbe.domain.trip.model.TripLikedItem;
import org.tenten.tentenbe.domain.trip.model.TripLikedItemPreference;
import org.tenten.tentenbe.domain.trip.model.TripMember;
import org.tenten.tentenbe.domain.trip.repository.*;
import org.tenten.tentenbe.global.common.enums.Category;
import org.tenten.tentenbe.global.common.enums.TripAuthority;
import org.tenten.tentenbe.global.common.enums.TripStatus;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripItemRepository tripItemRepository;
    private final TripMemberRepository tripMemberRepository;
    private final TourItemRepository tourItemRepository;
    private final MemberRepository memberRepository;
    private final TripLikedItemRepository tripLikedItemRepository;
    private final TripLikedItemPreferenceRepository tripLikedItemPreferenceRepository;

    @Transactional
    public TripCreateResponse createTrip(Long memberId, TripCreateRequest request) {
        Member member = getMemberOrNullById(memberId);
        Long numberOfTrip = tripMemberRepository.countTripMemberByMember(member) + 1L;
        Trip trip = Trip.builder()
            .tripName(request.tripName()
                .orElse("나의 "+numberOfTrip+"번째 여정계획"))
            .numberOfPeople(request.numberOfPeople().orElse(1L))
            .startDate(request.startDate().orElse(LocalDate.now()))
            .endDate(request.endDate().orElse(LocalDate.now()))
            .area(request.area().orElse(null))
            .subarea(request.subarea().orElse(null))
            .tripStatus(TripStatus.BEFORE)
            .budget(0L)
            .build();
        TripMember tripMember = TripMember.builder()
            .member(member)
            .trip(trip)
            .tripAuthority(TripAuthority.WRITE)
            .build();
        tripMemberRepository.save(tripMember);

        return new TripCreateResponse(tripRepository.save(trip).getId());
    }

    @Transactional(readOnly = true)
    public Page<TripSimpleResponse> getTrips(Long memberId, Pageable pageable) {
        getMemberOrNullById(memberId);
        return tripRepository.findTripsByMemberId(memberId, pageable);
    }

    @Transactional(readOnly = true)
    public TripDetailResponse getTrip(Long memberId, Long tripId) {
        getMemberOrNullById(memberId);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        return new TripDetailResponse(
            trip.getTripName(),
            trip.getStartDate(),
            trip.getEndDate()
        );
    }

    @Transactional
    public TripInfoUpdateResponse updateTrip(Long memberId, Long tripId, TripInfoUpdateRequest request) {
        Member member = getMemberOrNullById(memberId);
//        validateWriter(member);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        TripInfoUpdateResponse tripInfoUpdateResponse = trip.updateTripInfo(request);
        tripRepository.save(trip);
        return tripInfoUpdateResponse;
    }

    @Transactional
    public void deleteTripMember(Long memberId, Long tripId) {

    }

    @Transactional
    public void LikeTourInOurTrip(Long memberId, Long tripId, TripLikedItemRequest request) {
        Member member = getMemberOrNullById(memberId);
//        validateWriter(member);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        request.tourItemIds().stream()
            .map(tourItemId -> tourItemRepository.findById(tourItemId)
                .orElseThrow(() -> new TourException("아이디에 해당하는 여행지가 없습니다. tourItemId : " + tourItemId, NOT_FOUND)))
            .map(tourItem -> TripLikedItem.builder()
                .trip(trip)
                .tourItem(tourItem).build())
            .forEach(tripLikedItemRepository::save);
    }

    @Transactional(readOnly = true)
    public Page<TripLikedSimpleResponse> getTripLikedItems(Long memberId, Long tripId, String categoryName, Pageable pageable) {
        Member member = getMemberOrNullById(memberId);
//        validateWriter(member);
        Long categoryCode = findCategoryCode(categoryName);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        tripLikedItemRepository.findTripLikedItemsById(memberId, tripId, categoryCode, pageable);

        return null;
    }

    private Long findCategoryCode(String categoryName) {
        if (categoryName != null) {
            return Category.fromName(categoryName).getCode();
        }
        return null;
    }

    @Transactional
    public void preferOrNotTourInOurTrip(Long memberId, Long tripId, Long tourItemId, Boolean prefer, Boolean notPrefer) {
        Member member = getMemberOrNullById(memberId);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        TourItem tourItem = tourItemRepository.findById(tourItemId)
            .orElseThrow(() -> new TourException("아이디에 해당하는 여행지가 없습니다. tourItemId : " + tourItemId, NOT_FOUND));
        TripMember tripMember = tripMemberRepository.findByMemberAndTrip(member, trip)
            .orElseThrow(() -> new TripMemberException("해당 회원은 여정에 속해있지 않은 회원입니다. memberId : "+ memberId, NOT_FOUND));
        TripLikedItem tripLikedItem = tripLikedItemRepository.findByTripAndTourItem(trip, tourItem)
            .orElseThrow(() -> new TripLikedItemException("해당 여행지는 우리의 관심목록에 속해있지 않은 여행지입니다. tourItemId : " + tourItemId, NOT_FOUND));

        if (Boolean.TRUE.equals(prefer) && Boolean.TRUE.equals(notPrefer)) {
            throw new TripLikeItemPreferenceException("좋아요와 싫어요를 동시에 누를 수 없습니다.", NOT_ACCEPTABLE);
        }

        TripLikedItemPreference tripLikedItemPreference = tripLikedItemPreferenceRepository
            .findByTripMemberAndTripLikedItem(tripMember, tripLikedItem)
            .orElseGet(() -> TripLikedItemPreference.builder()
                .tripMember(tripMember)
                .tripLikedItem(tripLikedItem)
                .build());

        tripLikedItemPreference.setPreferAndNotPrefer(prefer, notPrefer);

        tripLikedItemPreferenceRepository.save(tripLikedItemPreference);
    }

    @Transactional(readOnly = true)
    public TripSurveyResponse getTripSurveys(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        return null;
    }

    private Member getMemberOrNullById(Long memberId) { //TODO : 현재 코드는 로그인되어있는 회원만 여정조회 가능
        if(memberId != null) {
            return memberRepository.getReferenceById(memberId);
        }
        throw new MemberException("memberId가 유효하지 않습니다.", NOT_FOUND);
    }

//    private void validateWriter(Member member) {
//        tripMemberRepository.findByMember(member)
//            .orElseThrow(() -> new TripException("해당 아이디의 회원은 편집권한이 없습니다. memberId : " + member.getId(), NOT_FOUND));
//    }
}
