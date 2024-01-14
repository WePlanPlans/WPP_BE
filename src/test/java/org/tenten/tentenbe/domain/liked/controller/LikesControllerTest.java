package org.tenten.tentenbe.domain.liked.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.tenten.tentenbe.common.ControllerTest;
import org.tenten.tentenbe.config.WithMockCustomUser;
import org.tenten.tentenbe.domain.liked.service.LikedService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LikesControllerTest extends ControllerTest {
    @MockBean
    private LikedService likedService;


    @Test
    @WithMockCustomUser
    @DisplayName("좋아요 등록")
    public void updateCommentSuccess() throws Exception {

        mockMvc.perform(post("/api/liked/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SUCCESS"))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("좋아요 삭제")
    public void deleteReviewSuccess() throws Exception {

        willDoNothing().given(likedService).cancelLikeTour(any(),any());

        mockMvc.perform(delete("/api/liked/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("DELETED"))
                .andDo(print());
    }

}
