package org.tenten.tentenbe.domain.trip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.member.dto.response.MemberSimpleInfo;
import org.tenten.tentenbe.domain.member.exception.MemberException;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.model.Survey;
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

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class TripService {
    private static final String SECRET_KEY = "1234567890123456";
    private final TripRepository tripRepository;
    private final TripItemRepository tripItemRepository;
    private final TripMemberRepository tripMemberRepository;
    private final TourItemRepository tourItemRepository;
    private final MemberRepository memberRepository;
    private final TripLikedItemRepository tripLikedItemRepository;
    private final TripLikedItemPreferenceRepository tripLikedItemPreferenceRepository;

    @Transactional
    public TripCreateResponse createTrip(Long memberId, TripCreateRequest request) {
        Member member = getMemberById(memberId);
        Long numberOfTrip = tripMemberRepository.countTripMemberByMember(member) + 1L;

        String joinCode = createJoinCode();
        String encryptedJoinCode = encryptJoinCode(joinCode);

        Trip trip = Trip.builder()
            .tripName(request.tripName()
                .orElse("나의 "+numberOfTrip+"번째 여정계획"))
            .numberOfPeople(request.numberOfPeople().orElse(1L))
            .startDate(request.startDate().orElse(LocalDate.now()))
            .endDate(request.endDate().orElse(LocalDate.now()))
            .isDeleted(false)
            .area(request.area().orElse(null))
            .subarea(request.subarea().orElse(null))
            .budget(0L)
            .transportationPriceSum(0L)
            .tripItemPriceSum(0L)
            .joinCode(encryptedJoinCode)
            .build();
        TripMember tripMember = TripMember.builder()
            .member(member)
            .trip(trip)
            .tripAuthority(TripAuthority.WRITE)
            .build();
        tripMemberRepository.save(tripMember);

        return new TripCreateResponse(tripRepository.save(trip).getId());
    }

    private String createJoinCode() {
        Random random = new Random();
        int number = random.nextInt(90000) + 10000;
        return String.valueOf(number);
    }

    private String encryptJoinCode(String joinCode) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(joinCode.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("참여 코드 암호화 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional(readOnly = true)
    public List<TripSimpleResponse> getTrips(Long memberId) {
        getMemberById(memberId);
        return tripRepository.findTripsByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public TripDetailResponse getTrip(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        return new TripDetailResponse(
            trip.getTripName(),
            trip.getStartDate(),
            trip.getEndDate(),
            trip.getNumberOfPeople()
        );
    }

    @Transactional
    public TripInfoUpdateResponse updateTrip(Long memberId, Long tripId, TripInfoUpdateRequest request) {
        Member member = getMemberById(memberId);
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
        Member member = getMemberById(memberId);
//        validateWriter(member);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        request.tourItemIds().stream()
            .map(tourItemId -> tourItemRepository.findById(tourItemId)
                .orElseThrow(() -> new TourException("아이디에 해당하는 여행지가 없습니다. tourItemId : " + tourItemId, NOT_FOUND)))
            .forEach(tourItem -> {
                Optional<TripLikedItem> existingTripLikedItem = tripLikedItemRepository.findByTripAndTourItem(trip, tourItem);
                if (existingTripLikedItem.isEmpty()) {
                    TripLikedItem newTripLikedItem = TripLikedItem.builder()
                        .tourItem(tourItem)
                        .trip(trip)
                        .build();
                    tripLikedItemRepository.save(newTripLikedItem);
                }
            });
    }

    @Transactional(readOnly = true)
    public Page<TripLikedSimpleResponse> getTripLikedItems(Long memberId, Long tripId, String categoryName, Pageable pageable) {
        Member member = getMemberOrNullById(memberId);
//        validateWriter(member);
        Long categoryCode = findCategoryCode(categoryName);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        return tripLikedItemRepository.findTripLikedItemsById(memberId, tripId, categoryCode, pageable);
    }

    private Member getMemberOrNullById(Long memberId) {
        if(memberId != null) {
            return memberRepository.getReferenceById(memberId);
        }
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
        Member member = getMemberById(memberId);
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
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : " + tripId, NOT_FOUND));

        List<TripMember> tripMembers = tripMemberRepository.findByTrip(trip);

        SurveyCounter counter = new SurveyCounter();
        tripMembers.stream()
            .map(TripMember::getMember)
            .map(Member::getSurvey)
            .forEach(counter::countSurvey);

        return new TripSurveyResponse(
            counter.planningTotal, counter.planningCount,
            counter.activeHoursTotal, counter.activeHoursCount,
            counter.accommodationTotal, counter.accommodationCount,
            counter.foodTotal, counter.foodCount,
            counter.tripStyleTotal, counter.tripStyleCount,
            counter.tripSurveyMemberCount
        );
    }

    private static class SurveyCounter {
        long planningTotal = 0, planningCount = 0;
        long activeHoursTotal = 0, activeHoursCount = 0;
        long accommodationTotal = 0, accommodationCount = 0;
        long foodTotal = 0, foodCount = 0;
        long tripStyleTotal = 0, tripStyleCount = 0;
        long tripSurveyMemberCount = 0;

        void countSurvey(Survey survey) {
            if (survey != null) {
                incrementIfNotNull(survey.getPlanning(), "철저하게", this::incrementPlanning);
                incrementIfNotNull(survey.getActiveHours(), "아침형", this::incrementActiveHours);
                incrementIfNotNull(survey.getAccommodation(), "분위기", this::incrementAccommodation);
                incrementIfNotNull(survey.getFood(), "노포", this::incrementFood);
                incrementIfNotNull(survey.getTripStyle(), "액티비티", this::incrementTripStyle);
                tripSurveyMemberCount++;
            }
        }

        private void incrementIfNotNull(String value, String expectedValue, Runnable incrementMethod) {
            if (value != null) {
                incrementMethod.run();
                if (expectedValue.equals(value)) {
                    incrementMethod.run();
                }
            }
        }

        private void incrementPlanning() {
            planningTotal++;
        }
        private void incrementActiveHours() {
            activeHoursTotal++;
        }
        private void incrementAccommodation() {
            accommodationTotal++;
        }
        private void incrementFood() {
            foodTotal++;
        }
        private void incrementTripStyle() {
            tripStyleTotal++;
        }
    }

    public TripSurveyMemberResponse getTripSurveyMember(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : " + tripId, NOT_FOUND));

        List<TripMember> tripMembers = tripMemberRepository.findByTrip(trip);

        List<MemberSimpleInfo> tripSurveySetMemberInfos = new ArrayList<>();
        List<MemberSimpleInfo> nonTripSurveySetMemberInfos = new ArrayList<>();
        long tripSurveyMemberCount = 0;

        for (TripMember tripMember : tripMembers) {
            Member member = tripMember.getMember();
            if (member.getSurvey() != null) {
                tripSurveySetMemberInfos.add(new MemberSimpleInfo(member.getId(), member.getNickname(), member.getProfileImageUrl()));
                tripSurveyMemberCount++;
            } else {
                nonTripSurveySetMemberInfos.add(new MemberSimpleInfo(member.getId(), member.getNickname(), member.getProfileImageUrl()));
            }
        }

        return new TripSurveyMemberResponse(tripSurveyMemberCount, tripSurveySetMemberInfos, nonTripSurveySetMemberInfos);
    }

    @Transactional
    public void joinTrip(Long memberId, Long tripId, String joinCode) {
        Member member = getMemberById(memberId);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("해당 여정이 존재하지 않습니다.", NOT_FOUND));

        String decryptedJoinCode = decryptedJoinCode(trip.getJoinCode());

        if (!decryptedJoinCode.equals(joinCode)) {
            throw new TripException("참여 코드가 일치하지 않습니다.", UNAUTHORIZED);
        }

        TripMember tripMember = TripMember.builder()
            .member(member)
            .trip(trip)
            .tripAuthority(TripAuthority.WRITE)
            .build();
        tripMemberRepository.save(tripMember);
    }

    @Transactional(readOnly = true)
    public String getJoinCode(Long memberId, Long tripId) {
        Member member = getMemberById(memberId);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("해당 여정이 존재하지 않습니다.", NOT_FOUND));

        tripMemberRepository.findByMember(member)
            .orElseThrow(() -> new TripMemberException("해당 회원은 참여코드를 조회할 권한이 없습니다.", NOT_ACCEPTABLE));

        return decryptedJoinCode(trip.getJoinCode());
    }

    private String decryptedJoinCode(String encryptedJoinCode) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedJoinCode));
            return new String(original);
        } catch (Exception e) {
            throw new RuntimeException("참여 코드 복호화 중 오류가 발생했습니다.", e);
        }
    }

    private Member getMemberById(Long memberId) {
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
