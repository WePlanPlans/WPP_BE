package org.tenten.tentenbe.domain.trip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tenten.tentenbe.domain.trip.repository.TripItemRepository;
import org.tenten.tentenbe.domain.trip.repository.TripRepository;
import org.tenten.tentenbe.domain.trip.repository.TripUserRepository;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripItemRepository tripItemRepository;
    private final TripUserRepository tripUserRepository;
}
