package org.tenten.tentenbe.domain.trip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.trip.dto.request.TripCreateRequest;
import org.tenten.tentenbe.domain.trip.dto.request.TripLikedItemRequest;
import org.tenten.tentenbe.domain.trip.dto.response.*;
import org.tenten.tentenbe.domain.trip.model.Trip;
import org.tenten.tentenbe.domain.trip.model.TripMember;
import org.tenten.tentenbe.domain.trip.repository.TripItemRepository;
import org.tenten.tentenbe.domain.trip.repository.TripRepository;
import org.tenten.tentenbe.domain.trip.repository.TripMemberRepository;
import org.tenten.tentenbe.global.common.enums.TripAuthority;
import org.tenten.tentenbe.global.common.enums.TripStatus;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripItemRepository tripItemRepository;
    private final TripMemberRepository tripUserRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public TripCreateResponse createTrip(Long memberId, TripCreateRequest request) {
        Member member = memberRepository.getReferenceById(memberId);
        Long numberOfTrip = tripUserRepository.countTripMemberByMember(member) + 1L;
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
        tripUserRepository.save(tripMember);

        return new TripCreateResponse(tripRepository.save(trip).getId());
    }

    @Transactional(readOnly = true)
    public Page<TripSimpleResponse> getTrips(Long memberId, Pageable pageable) {
        Member member = memberRepository.getReferenceById(memberId);
        return tripRepository.findTripsByMemberId(memberId, pageable);
    }

    @Transactional
    public void deleteTripMember(Long memberId, Long tripId) {

    }

    public Page<TripLikedSimpleResponse> getTripLikedItems(Long tripId, String category, Pageable pageable) {
        return null;
    }

    public TripSurveyResponse getTripSurveys(Long tripId) {
        return null;
    }

    public void LikeTourInOurTrip(Long tripId, TripLikedItemRequest request) {

    }

    public void preferOrNotTourInOurTrip(Long memberId, Long tripId, Long tourId, Boolean prefer) {

    }

    public TripDetailResponse getTrip(Long memberId, Long tripId) {
        return null;
    }

    public TripInfoUpdateResponse updateTrip(Long memberId, Long tripId) {
        return null;
    }
}
