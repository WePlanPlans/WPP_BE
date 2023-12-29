package org.tenten.tentenbe.domain.trip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.trip.dto.request.TripCreateRequest;
import org.tenten.tentenbe.domain.trip.dto.response.TripResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse;
import org.tenten.tentenbe.domain.trip.repository.TripItemRepository;
import org.tenten.tentenbe.domain.trip.repository.TripRepository;
import org.tenten.tentenbe.domain.trip.repository.TripUserRepository;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripItemRepository tripItemRepository;
    private final TripUserRepository tripUserRepository;
    @Transactional
    public TripSimpleResponse createTrip(Long memberId, TripCreateRequest tripCreateRequest) {
        return null;
    }
    @Transactional(readOnly = true)
    public TripResponse getTrips(Long memberId, Pageable pageable) {
        return null;
    }
    @Transactional
    public void deleteTripMember(Long memberId, Long tripId) {

    }
}
