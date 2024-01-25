package org.tenten.tentenbe.common.fixture;

import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.domain.trip.dto.request.TripCreateRequest;
import org.tenten.tentenbe.domain.trip.dto.request.TripInfoUpdateRequest;
import org.tenten.tentenbe.domain.trip.dto.request.TripLikedItemRequest;
import org.tenten.tentenbe.domain.trip.dto.response.*;
import org.tenten.tentenbe.domain.trip.model.*;
import org.tenten.tentenbe.global.common.enums.TripAuthority;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.global.common.enums.Transportation.PUBLIC_TRANSPORTATION;
import static org.tenten.tentenbe.global.common.enums.TripAuthority.READ_ONLY;

public class TripFixture {

    public static Trip trip(){
        return Trip.builder()
                .id(10L)
                .tripName("test trip name")
                .numberOfPeople(4L)
                .startDate(LocalDate.parse("2024-01-01"))
                .endDate(LocalDate.parse("2024-01-06"))
                .isDeleted(false)
                .area(null)
                .subarea(null)
                .budget(10L)
                .build();
    }

    public static TripItem savetripItem(Trip trip,TourItem tourItem){
        return TripItem.builder()
                .transportation(PUBLIC_TRANSPORTATION)
                .seqNum(1L)
                .visitDate(LocalDate.parse("2024-01-01"))
                .price(200000L)
                .trip(trip)
                .tourItem(tourItem)
                .build();

    }
    public static TripCreateRequest tripCreateRequest(){
        return new TripCreateRequest(
                Optional.of("my first trip"),
                Optional.of(4L),
                Optional.of(LocalDate.parse("2024-01-01")),
                Optional.of(LocalDate.parse("2024-01-06")),
                Optional.of("강원도"),
                Optional.of("강릉시")
        );
    }

    public static TripSimpleResponse tripSimpleResponse(){
        return new TripSimpleResponse(
                1L,
                "my first trip response",
                LocalDate.parse("2024-01-01"),
                LocalDate.parse("2024-01-06"),
                4L,
                "traveling",
                "tripThumbnail.com"
        );
    }

    public static TripDetailResponse tripDetailResponse(){
        return new TripDetailResponse(
                "trip name test",
                LocalDate.parse("2024-01-01"),
                LocalDate.parse("2024-01-06")
        );
    }

    public static TripInfoUpdateResponse tripInfoUpdateResponse(){
        return new TripInfoUpdateResponse(
                "my second trip response",
                4L,
                LocalDate.parse("2024-01-01"),
                LocalDate.parse("2024-01-06"),
                "서울시",
                "강남구"
        );

    }

    public static TripInfoUpdateRequest tripInfoUpdateRequest(){
        return new TripInfoUpdateRequest(
                "my second trip request",
                4L,
                LocalDate.parse("2024-01-01"),
                LocalDate.parse("2024-01-06"),
                "서울시",
                "강남구"
        );

    }

    public static TripLikedSimpleResponse tripLikedSimpleResponse(){
        return new TripLikedSimpleResponse(
                1L,
                1L,
                1L,
                2.5d,
                10L,
                true,
                false,
                4L,
                3L

        );
    }

    public static TripLikedItemRequest tripLikedItemRequest(){
        return new TripLikedItemRequest(List.of(1L, 2L, 3L, 4L));
    }

    public static TripSurveyResponse tripSurveyResponse(){
        return new TripSurveyResponse(
                20L,
                10L,
                30L,
                40L,
                4L,
                2L,
                4L,
                3L,
                8L,
                3L

        );
    }

    public static TripMember savedTripMember(Member member, Trip trip){
        return TripMember.builder()
                .member(member)
                .trip(trip)
                .tripAuthority(TripAuthority.WRITE)
                .build();
    }

    public static TripLikedItem saveTripLikedItem(Trip trip,TourItem tourItem){
        return TripLikedItem.builder()
                .id(1L)
                .trip(trip)
                .tourItem(tourItem)
                .build();
    }

    public static TripLikedItemPreference saveTripLikedItemPreference(TripMember tripMember,TripLikedItem tripLikedItem){
        return TripLikedItemPreference.builder()
                .id(1L)
                .prefer(true)
                .notPrefer(false)
                .tripMember(tripMember)
                .tripLikedItem(tripLikedItem)
                .build();


    }



}
