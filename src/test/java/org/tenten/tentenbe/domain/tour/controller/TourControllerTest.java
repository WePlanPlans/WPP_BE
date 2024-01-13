package org.tenten.tentenbe.domain.tour.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.tenten.tentenbe.common.ControllerTest;
import org.tenten.tentenbe.domain.review.service.ReviewService;
import org.tenten.tentenbe.domain.tour.dto.response.TourDetailResponse;
import org.tenten.tentenbe.domain.tour.service.TourService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.common.fixture.ReviewFixture.reviewResponse;
import static org.tenten.tentenbe.common.fixture.TourFixture.tourSimpleResponsePage;

public class TourControllerTest extends ControllerTest {

    @MockBean
    private TourService tourService;
    @MockBean
    private ReviewService reviewService;
    @Test
    @DisplayName("인기 여행지 조회 성공")
    public void searchFamousTourSuccess() throws Exception {

        given(tourService.getTours(any(), any(), any())).willReturn(tourSimpleResponsePage());

        mockMvc.perform(get("/api/tours"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.[0].title").value("test title"))
                .andExpect(jsonPath("$.data.content.[0].tourAddress").value("test address"))
                .andExpect(jsonPath("$.data.content.[0].longitude").value("longitude"))
                .andExpect(jsonPath("$.data.content.[0].reviewCount").value(10L))
                .andExpect(jsonPath("$.data.content.[0].likedCount").value(20L))
                .andDo(print());
    }

    @Test
    @DisplayName("여행지 검색 성공")
    public void searchTourSuccess() throws Exception {
        given(tourService.searchTours(any(), any(), any(),any(),any())).willReturn(tourSimpleResponsePage());

        mockMvc.perform(get("/api/tours/search?searchWord=test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.[0].title").value("test title"))
                .andExpect(jsonPath("$.data.content.[0].tourAddress").value("test address"))
                .andExpect(jsonPath("$.data.content.[0].longitude").value("longitude"))
                .andExpect(jsonPath("$.data.content.[0].reviewCount").value(10L))
                .andExpect(jsonPath("$.data.content.[0].likedCount").value(20L))
                .andDo(print());
    }
    @Test
    @DisplayName("여행지 상세 조회 성공")
    public void searchSpecificTourSuccess() throws Exception {
        TourDetailResponse tourSimpleResponse = new TourDetailResponse(tourItem(), true, "full address");

        given(tourService.getTourDetail(any(), any())).willReturn(tourSimpleResponse);

        mockMvc.perform(get("/api/tours/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("test title"))
                .andExpect(jsonPath("$.data.fullAddress").value("full address"))
                .andExpect(jsonPath("$.data.longitude").value("longitude"))
                .andExpect(jsonPath("$.data.tel").value("test telephone"))
                .andExpect(jsonPath("$.data.liked").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("여행 상품 리뷰 조회 성공")
    public void reviewTourSuccess() throws Exception {

        given(reviewService.getTourReviews(any(), any(),any())).willReturn(reviewResponse());

        mockMvc.perform(get("/api/tours/10/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.ratingAverage").value(20.2))
                .andExpect(jsonPath("$.data.reviewTotalCount").value(10L))
                .andExpect(jsonPath("$.data.keywordTotalCount").value(15L))
                .andExpect(jsonPath("$.data.reviewInfos.content.[0].authorNickname").value("nickNameTest"))
                .andExpect(jsonPath("$.data.reviewInfos.content.[0].content").value("hello world"))
                .andDo(print());
    }

}
