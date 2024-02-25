package org.tenten.tentenbe.domain.trip.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tenten.tentenbe.domain.trip.repository.TripLikedItemPreferenceRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TripServiceTest {

    @Autowired
    private TripService tripService;

    @Autowired
    private TripLikedItemPreferenceRepository tripLikedItemPreferenceRepository;

    @Test
    public void 좋아요_1회_요청() {
        tripService.preferOrNotTourInOurTrip(1L, "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b", 1L, false, true);
        assertEquals(1, tripLikedItemPreferenceRepository.countByTripLikedItemIdAndPrefer(1L, false));
    }

    @Test
    public void 동시에_100개의_좋아요_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            Long memberId = (long) i + 1;
            executorService.submit(() -> {
                try {
                    tripService.preferOrNotTourInOurTrip(memberId, "6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b", 1L, false, true);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // latch의 카운트가 0이 될 때까지 메인 스레드를 차단합니다.

        assertEquals(100, tripLikedItemPreferenceRepository.countByTripLikedItemIdAndPrefer(1L, false));
    }
}