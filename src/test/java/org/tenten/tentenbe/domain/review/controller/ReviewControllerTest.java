package org.tenten.tentenbe.domain.review.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.tenten.tentenbe.common.ControllerTest;
import org.tenten.tentenbe.config.WithMockCustomUser;
import org.tenten.tentenbe.domain.review.dto.request.ReviewCreateRequest;
import org.tenten.tentenbe.domain.review.dto.request.ReviewUpdateRequest;
import org.tenten.tentenbe.domain.review.service.ReviewService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tenten.tentenbe.common.fixture.CommentFixture.commentResponse;
import static org.tenten.tentenbe.common.fixture.ReviewFixture.*;

public class ReviewControllerTest extends ControllerTest {

    @MockBean
    private ReviewService reviewService;

    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 작성")
    public void createReviewSuccess() throws Exception {

        ReviewCreateRequest reviewCreateRequest = reviewCreateRequest();

        given(reviewService.createReview(any(), any())).willReturn(reviewInfo());

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(reviewCreateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.reviewId").value(1L))
                .andExpect(jsonPath("$.data.authorNickname").value("nickNameTest"))
                .andExpect(jsonPath("$.data.authorProfileImageUrl").value("naver.com"))
                .andExpect(jsonPath("$.data.rating").value(20.5))
                .andExpect(jsonPath("$.data.content").value("hello world"))
                .andExpect(jsonPath("$.data.content").value("hello world"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 수정")
    public void updateReviewSuccess() throws Exception {

        ReviewUpdateRequest reviewUpdateRequest = reviewUpdateRequest();

        given(reviewService.updateReview(any(),any(),any())).willReturn(reviewInfo());

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(reviewUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.reviewId").value(1L))
                .andExpect(jsonPath("$.data.authorNickname").value("nickNameTest"))
                .andExpect(jsonPath("$.data.authorProfileImageUrl").value("naver.com"))
                .andExpect(jsonPath("$.data.rating").value(20.5))
                .andExpect(jsonPath("$.data.content").value("hello world"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 삭제")
    public void deleteReviewSuccess() throws Exception {

        willDoNothing().given(reviewService).deleteReview(any(),any());

        mockMvc.perform(delete("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("DELETED"))
                .andDo(print());;
    }

    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 댓글조회")
    public void getReviewDetailSuccess() throws Exception {

        given(reviewService.getReviewComments(any(),any(),any())).willReturn(commentResponse());

        mockMvc.perform(get("/api/reviews/1/comments?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.comments.content.[0].authorNickname").value("comment Nick name"))
                .andExpect(jsonPath("$.data.comments.content.[0].authorProfileImageUrl").value("profile image url"))
                .andExpect(jsonPath("$.data.comments.content.[0].content").value("comment test content"))
                .andExpect(jsonPath("$.data.comments.content.[0].isAuthor").value(true))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 키워드 조회")
    public void getKeyWordSuccess() throws Exception {
        given(reviewService.getKeywords(any())).willReturn(keywordResponse());

        mockMvc.perform(get("/api/reviews/keywords?keywordType=DINING_KEYWORD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.keywords.[0].content").value("save keyword content"))
                .andExpect(jsonPath("$.data.keywords.[0].type").value("DINING_KEYWORD"))
                .andDo(print());

    }

}
