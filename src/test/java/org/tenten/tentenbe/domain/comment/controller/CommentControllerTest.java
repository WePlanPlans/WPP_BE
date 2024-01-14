package org.tenten.tentenbe.domain.comment.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.tenten.tentenbe.common.ControllerTest;
import org.tenten.tentenbe.config.WithMockCustomUser;
import org.tenten.tentenbe.domain.comment.exception.CommentException;
import org.tenten.tentenbe.domain.comment.service.CommentService;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tenten.tentenbe.common.fixture.CommentFixture.*;

public class CommentControllerTest extends ControllerTest {
    @MockBean
    private CommentService commentService;

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 작성 성공")
    public void createCommentSuccess() throws Exception {

        given(commentService.createComment(any(), any())).willReturn(commentInfo());

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentCreateRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("comment test content"))
                .andExpect(jsonPath("$.data.authorNickname").value("comment Nick name"))
                .andExpect(jsonPath("$.data.authorProfileImageUrl").value("profile image url"))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("댓글 수정 성공")
    public void updateCommentSuccess() throws Exception {
        given(commentService.updateComment(any(),any(),any())).willReturn(commentInfo());

        mockMvc.perform(put("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentUpdateRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("comment test content"))
                .andExpect(jsonPath("$.data.authorNickname").value("comment Nick name"))
                .andExpect(jsonPath("$.data.authorProfileImageUrl").value("profile image url"))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("리뷰 삭제 성공")
    public void deleteReviewSuccess() throws Exception {

        willDoNothing().given(commentService).deleteComment(any(),any());

        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("DELETED"))
                .andDo(print());
    }
    @Test
    @WithMockCustomUser()
    @DisplayName("리뷰 수정시 권한이 없을경우 401 에러 발생")
    public void anonymousUpdateFail() throws Exception {

        given(commentService.updateComment(any(), any(), any()))
                .willThrow(new CommentException("댓글 수정 권한이 없습니다.", HttpStatus.UNAUTHORIZED));

        mockMvc.perform(put("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentUpdateRequest())))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockCustomUser()
    @DisplayName("리뷰 삭제시 권한이 없을경우 401 에러 발생")
    public void anonymousDeleteFail() throws Exception {

        doThrow(new CommentException("댓글 수정 권한이 없습니다.", HttpStatus.UNAUTHORIZED))
                .when(commentService).deleteComment(any(), any());

        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

}
