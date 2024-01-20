package org.tenten.tentenbe.domain.trip.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
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
import org.tenten.tentenbe.domain.trip.dto.request.TripItemRequest;
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

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.tenten.tentenbe.domain.trip.dto.response.TripMembersResponse.TripMemberSimpleInfo;
import static org.tenten.tentenbe.global.common.enums.Transportation.CAR;

@Slf4j
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
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public TripCreateResponse createTrip(Long memberId, TripCreateRequest request) {
        Member member = getMemberById(memberId);
        Long numberOfTrip = tripMemberRepository.countTripMemberByMember(member) + 1L;

        String joinCode = createJoinCode();
        String encryptedJoinCode = encryptJoinCode(joinCode);

        HashMap<String, Integer> tripPathPriceMap = new HashMap<>();
        LocalDate startDate = request.startDate().orElse(LocalDate.now());
        LocalDate endDate = request.endDate().orElse(LocalDate.now());
        HashMap<String, String> tripTransportationMap = new HashMap<>();
        tripPathPriceMap.put(startDate.toString(), 0);
        tripTransportationMap.put(endDate.toString(), CAR.getName());
        Trip trip = Trip.builder()
            .tripName(request.tripName()
                .orElse("나의 "+numberOfTrip+"번째 여정계획"))
            .numberOfPeople(request.numberOfPeople().orElse(1L))
            .startDate(startDate)
            .endDate(endDate)
            .isDeleted(false)
            .budget(0L)
            .transportationPriceSum(0L)
            .tripItemPriceSum(0L)
            .joinCode(encryptedJoinCode)
            .tripPathPriceMap(tripPathPriceMap)
            .tripTransportationMap(tripTransportationMap)
            .build();
        TripMember tripMember = TripMember.builder()
            .member(member)
            .trip(trip)
            .tripAuthority(TripAuthority.WRITE)
            .build();
        tripMemberRepository.save(tripMember);

        Trip savedTrip = tripRepository.save(trip);
        savedTrip.updatedEncryptedId(encryptJoinCode(Long.toString(savedTrip.getId())));
        return new TripCreateResponse(savedTrip.getId());
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
    public TripAuthorityResponse getTripAuthority(Long memberId, Long tripId) {
        if (memberId == null) {
            return new TripAuthorityResponse(null, TripAuthority.READ_ONLY, tripId);
        }
        Member member = getMemberById(memberId);
        TripAuthority tripAuthority = tripMemberRepository.findByMemberAndTrip(member, tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND)))
            .orElse(TripMember.builder()
                .tripAuthority(TripAuthority.READ_ONLY)
                .build())
            .getTripAuthority();
        return new TripAuthorityResponse(member.getId(), tripAuthority, tripId);
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
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        validateWriter(member, trip);
        TripInfoUpdateResponse tripInfoUpdateResponse = trip.updateTripInfo(request);
        tripRepository.save(trip);
        return tripInfoUpdateResponse;
    }

    @Transactional
    public void deleteTripMember(Long memberId, Long tripId) {
        Member member = getMemberById(memberId);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        validateWriter(member, trip);
        TripMember tripMember = tripMemberRepository.findByMemberAndTrip(member, trip)
            .orElseThrow(() -> new TripMemberException("해당 회원은 여정에 속해있지 않은 회원입니다. memberId : "+ memberId, NOT_FOUND));
        tripMemberRepository.delete(tripMember);
        trip.getTripMembers().remove(tripMember);

        if(trip.getTripMembers().isEmpty()) {
            trip.setIsDeleted(true);
            tripRepository.save(trip);
        }
    }

    @Transactional
    public void LikeTourInOurTrip(Long memberId, Long tripId, TripLikedItemRequest request) {
        Member member = getMemberById(memberId);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        validateWriter(member, trip);
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
        getMemberOrNullById(memberId);
        tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        Long categoryCode = findCategoryCode(categoryName);
        return tripLikedItemRepository.findTripLikedItemsById(memberId, tripId, categoryCode, pageable);
    }

    private void getMemberOrNullById(Long memberId) {
        if(memberId != null) {
            memberRepository.getReferenceById(memberId);
        }
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

    @Transactional(readOnly = true)
    public TripMembersResponse getTripMembers(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));

        List<TripMemberSimpleInfo> tripMemberSimpleInfos = trip.getTripMembers().stream()
            .map(tripMember -> new TripMemberSimpleInfo(tripMember.getMember().getNickname(), tripMember.getMember().getProfileImageUrl()))
            .toList();

        return new TripMembersResponse(tripMemberSimpleInfos);
    }

    @Transactional
    public TripItemResponse addTripItem(Long memberId, Long tripId, TripItemRequest tripItemRequest) {
        Member member = getMemberById(memberId);
        Trip trip = tripRepository.findById(tripId)
            .orElseThrow(() -> new TripException("아이디에 해당하는 여정이 없습니다. tripId : "+ tripId, NOT_FOUND));
        validateWriter(member, trip);

        Map<String, String> tripItemRequestMap = new HashMap<>();
        tripItemRequestMap.put("tourItemId", tripItemRequest.tourItemId().toString());
        tripItemRequestMap.put("visitDate", tripItemRequest.visitDate().toString());
        String wsUrl = "https://ws.weplanplans.site/trips/" + tripId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<Map> request = new HttpEntity<>(tripItemRequestMap, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(wsUrl, POST, request, String.class);

        if (responseEntity.getStatusCode() == OK) {
            try {
                Map<String, Object> map = objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
                return new TripItemResponse(
                    tripId,
                    Long.valueOf(map.get("tripItemId").toString()),
                    Long.valueOf(map.get("tourItemId").toString()),
                    LocalDate.parse(map.get("visitDate").toString())
                );
            } catch (JsonProcessingException e) {
                throw new TripException("ws 서버로부터 받은 응답을 읽는데 실패하였습니다.", BAD_REQUEST);
            }
        } else {
            throw new TripException("ws 서버로부터 에러 응답을 받았습니다.", BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
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

        tripMemberRepository.findByMemberAndTrip(member, trip)
            .orElseThrow(() -> new TripMemberException("해당 회원은 참여코드를 조회할 권한이 없습니다. memberId : " + member.getId(), NOT_ACCEPTABLE));

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

    private void validateWriter(Member member, Trip trip) {
        tripMemberRepository.findByMemberAndTrip(member, trip)
            .orElseThrow(() -> new TripException("해당 아이디의 회원은 편집권한이 없습니다. memberId : " + member.getId(), NOT_FOUND));
    }
}
