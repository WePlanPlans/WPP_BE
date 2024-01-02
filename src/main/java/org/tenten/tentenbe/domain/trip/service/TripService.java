package org.tenten.tentenbe.domain.trip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.trip.dto.request.TripCreateRequest;
import org.tenten.tentenbe.domain.trip.dto.response.TripResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse;
import org.tenten.tentenbe.domain.trip.model.Trip;
import org.tenten.tentenbe.domain.trip.repository.TripItemRepository;
import org.tenten.tentenbe.domain.trip.repository.TripRepository;
import org.tenten.tentenbe.domain.trip.repository.TripUserRepository;
import org.tenten.tentenbe.global.common.enums.TripStatus;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripItemRepository tripItemRepository;
    private final TripUserRepository tripUserRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createTrip(Long memberId, TripCreateRequest request) {
        Member member = memberRepository.getReferenceById(memberId);
        Long numberOfTrip = tripUserRepository.countTripMemberByMember(member);
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

        return tripRepository.save(trip).getId();
    }
    @Transactional(readOnly = true)
    public TripResponse getTrips(Long memberId, Pageable pageable) {
        return null;
    }
    @Transactional
    public void deleteTripMember(Long memberId, Long tripId) {

    }
}
