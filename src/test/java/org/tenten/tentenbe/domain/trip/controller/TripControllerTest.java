package org.tenten.tentenbe.domain.trip.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.tenten.tentenbe.common.ControllerTest;
import org.tenten.tentenbe.config.WithMockCustomUser;
import org.tenten.tentenbe.domain.trip.dto.response.TripCreateResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripLikedSimpleResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse;
import org.tenten.tentenbe.domain.trip.service.TripService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tenten.tentenbe.common.fixture.TripFixture.*;

public class TripControllerTest extends ControllerTest {

    @MockBean
    private TripService tripService;

    @Test
    @DisplayName("여정 생성 성공")
    public void createTripSuccess() throws Exception {

        given(tripService.createTrip(any(), any())).willReturn(new TripCreateResponse(1L));

        mockMvc.perform(post("/api/trips")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(tripCreateRequest())))
                .andExpect(jsonPath("$.data.tripId").value(1L))
                .andDo(print());
    }

    @Test
    @DisplayName("나의 여정목록 조회 성공")
    public void getTripsSuccess() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);

        List<TripSimpleResponse> tripSimpleResponseList = List.of(tripSimpleResponse());
        Page<TripSimpleResponse> tripSimpleResponsePage = new PageImpl<>(tripSimpleResponseList, pageable, tripSimpleResponseList.size());

        given(tripService.getTrips(any(), any())).willReturn(tripSimpleResponsePage);

        mockMvc.perform(get("/api/trips"))
                .andExpect(jsonPath("$.data.content.[0].tripId").value(1L))
                .andExpect(jsonPath("$.data.content.[0].tripName").value("my first trip response"))
                .andExpect(jsonPath("$.data.content.[0].numberOfTripMembers").value(4L))
                .andDo(print());
    }

    @Test
    @DisplayName("여정 상세조회 성공")
    public void getDetailTripSuccess() throws Exception {

        given(tripService.getTrip(any())).willReturn(tripDetailResponse());

        mockMvc.perform(get("/api/trips/1"))
                .andExpect(jsonPath("$.data.tripName").value("trip name test"))
                .andExpect(jsonPath("$.data.startDate").value("2024-01-01"))
                .andExpect(jsonPath("$.data.endDate").value("2024-01-06"))
                .andDo(print());
    }

    @Test
    @DisplayName("여정 정보 수정 성공")
    public void updateTripSuccess() throws Exception {
        given(tripService.updateTrip(any(), any(), any())).willReturn(tripInfoUpdateResponse());

        mockMvc.perform(put("/api/trips/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(tripInfoUpdateRequest())))
                .andExpect(jsonPath("$.data.tripName").value("my second trip response"))
                .andExpect(jsonPath("$.data.startDate").value("2024-01-01"))
                .andExpect(jsonPath("$.data.endDate").value("2024-01-06"))
                .andExpect(jsonPath("$.data.area").value("서울시"))
                .andExpect(jsonPath("$.data.subarea").value("강남구"))
                .andDo(print());

    }

    @Test
    @DisplayName("여정 탈퇴 성공")
    public void deleteTripMemberSuccess() throws Exception {

        //TODO:: 코드 완성
        mockMvc.perform(delete("/api/trips/1"))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("그룹 관심 여행지 등록 성공")
    public void LikeTourInOurTripSuccess() throws Exception {

        mockMvc.perform(post("/api/trips/1/tripLikedTours")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(tripLikedItemRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andDo(print());

    }

    @Test
    @WithMockCustomUser
    @DisplayName("그룹 관심목록 조회 성공")
    public void getTripLikedItemsSuccess() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);

        List<TripLikedSimpleResponse> tripLikedSimpleResponses = List.of(tripLikedSimpleResponse());
        Page<TripLikedSimpleResponse> tripSimpleResponsePage = new PageImpl<>(tripLikedSimpleResponses, pageable, tripLikedSimpleResponses.size());

        given(tripService.getTripLikedItems(any(), any(), any(), any())).willReturn(tripSimpleResponsePage);

        mockMvc.perform(get("/api/trips/1/tripLikedTours"))
                .andExpect(jsonPath("$.data.content.[0].tripLikedItemId").value(1L))
                .andExpect(jsonPath("$.data.content.[0].ratingAverage").value(2.5))
                .andExpect(jsonPath("$.data.content.[0].reviewCount").value(10L))
                .andExpect(jsonPath("$.data.content.[0].notPreferTotalCount").value(3L))
                .andDo(print());
    }

    @Test
    @DisplayName("그룹 관심 여행지 좋아요 여부 확인 성공")
    public void preferOrNotTourInOurTripSuccess() throws Exception {

        mockMvc.perform(post("/api/trips/1/tripLikedTours/1?prefer=true&notPrefer=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andDo(print());
    }

    @Test
    @DisplayName("그룹 여행 취향 조회")
    public void getTripSurveysSuccess() throws Exception {

        given(tripService.getTripSurveys(any())).willReturn(tripSurveyResponse());

        mockMvc.perform(get("/api/trips/1/survey"))
                .andExpect(jsonPath("$.data.planningTotalCount").value(20L))
                .andExpect(jsonPath("$.data.planningCount").value(10L))
                .andExpect(jsonPath("$.data.activeHoursTotalCount").value(30L))
                .andExpect(jsonPath("$.data.activeHoursCount").value(40L))
                .andExpect(jsonPath("$.data.accommodationTotalCount").value(4L))
                .andExpect(jsonPath("$.data.accommodationCount").value(2L))
                .andDo(print());

    }

}
