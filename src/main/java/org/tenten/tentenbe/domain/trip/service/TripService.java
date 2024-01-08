package org.tenten.tentenbe.domain.trip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.tour.exception.TourException;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;
import org.tenten.tentenbe.domain.trip.dto.request.TripCreateRequest;
import org.tenten.tentenbe.domain.trip.dto.request.TripInfoUpdateRequest;
import org.tenten.tentenbe.domain.trip.dto.request.TripLikedItemRequest;
import org.tenten.tentenbe.domain.trip.dto.response.*;
import org.tenten.tentenbe.domain.trip.exception.TripException;
import org.tenten.tentenbe.domain.trip.model.Trip;
import org.tenten.tentenbe.domain.trip.model.TripLikedItem;
import org.tenten.tentenbe.domain.trip.model.TripMember;
import org.tenten.tentenbe.domain.trip.repository.TripItemRepository;
import org.tenten.tentenbe.domain.trip.repository.TripLikedItemRepository;
import org.tenten.tentenbe.domain.trip.repository.TripRepository;
import org.tenten.tentenbe.domain.trip.repository.TripMemberRepository;
import org.tenten.tentenbe.global.common.enums.Category;
import org.tenten.tentenbe.global.common.enums.TripAuthority;
import org.tenten.tentenbe.global.common.enums.TripStatus;

import java.time.LocalDate;

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
        Trip trip = tripRepository.getReferenceById(tripId);
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
        Trip trip = tripRepository.getReferenceById(tripId);
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
        Trip trip = tripRepository.getReferenceById(tripId);
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
        Trip trip = tripRepository.getReferenceById(tripId);
        tripLikedItemRepository.findTripLikedItemsById(memberId, tripId, categoryCode, pageable);

        return null;
    }

    private Long findCategoryCode(String categoryName) {
        if (categoryName != null) {
            return Category.fromName(categoryName).getCode();
        }
        return null;
    }

    public TripSurveyResponse getTripSurveys(Long tripId) {
        return null;
    }

    public void preferOrNotTourInOurTrip(Long memberId, Long tripId, Long tourId, Boolean prefer) {

    }

    private Member getMemberOrNullById(Long memberId) {
        if(memberId != null) {
            return memberRepository.getReferenceById(memberId);
        }
        throw new IllegalArgumentException("memberId가 유효하지 않습니다.");
    }

//    private void validateWriter(Member member) {
//        tripMemberRepository.findByMember(member)
//            .orElseThrow(() -> new TripException("해당 아이디의 회원은 편집권한이 없습니다. memberId : " + member.getId(), NOT_FOUND));
//    }
}
