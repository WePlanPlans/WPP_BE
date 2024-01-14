package org.tenten.tentenbe.domain.member.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.tenten.tentenbe.common.ControllerTest;
import org.tenten.tentenbe.config.WithMockCustomUser;
import org.tenten.tentenbe.domain.member.dto.request.MemberUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.request.PasswordUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.request.SurveyUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.response.MemberDetailResponse;
import org.tenten.tentenbe.domain.member.dto.response.MemberUpdateResponse;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.service.MemberService;
import org.tenten.tentenbe.domain.review.dto.response.MemberReviewResponse;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.tenten.tentenbe.common.fixture.AuthFixture.*;
import static org.tenten.tentenbe.common.fixture.MemberFixture.survey;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;
import static org.tenten.tentenbe.common.fixture.ReviewFixture.review;

public class MemberControllerTest extends ControllerTest {

    @MockBean
    private MemberService memberService;

    @Test
    @WithMockCustomUser
    @DisplayName("나의 관심 여행지 조회")
    public void myFavoriteToursSuccess() throws Exception {

        List<TourSimpleResponse> tourSimpleResponse = List.of(TourSimpleResponse.fromEntity(tourItem()));
        Pageable pageable = PageRequest.of(0, 10);

        given(memberService.getTours(any(), any())).willReturn(new PageImpl<>(tourSimpleResponse, pageable, 1));
        mockMvc.perform(get("/api/member/tours?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.[0].id").value(1L))
                .andExpect(jsonPath("$.data.content.[0].contentTypeId").value(1L))
                .andExpect(jsonPath("$.data.content.[0].title").value("test title"))
                .andExpect(jsonPath("$.data.content.[0].reviewCount").value(10L))
                .andExpect(jsonPath("$.data.content.[0].likedCount").value(20L))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("나의 리뷰 조회")
    public void getReviewsSuccess() throws Exception {

        List<MemberReviewResponse> memberReviewResponses = List.of(MemberReviewResponse.fromEntity(review()));
        Pageable pageable = PageRequest.of(0, 10);

        given(memberService.getReviews(any(), any())).willReturn(new PageImpl<>(memberReviewResponses, pageable, 1));
        mockMvc.perform(get("/api/member/reviews?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.[0].reviewId").value(1L))
                .andExpect(jsonPath("$.data.content.[0].authorNickname").value("nickNameTest"))
                .andExpect(jsonPath("$.data.content.[0].rating").value(20.5))
                .andExpect(jsonPath("$.data.content.[0].content").value("hello world"))
                .andExpect(jsonPath("$.data.content.[0].commentCount").value(0))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("회원 정보 조회")
    public void getMemberInfoSuccess() throws Exception {
        MemberDetailResponse memberDetailResponse = MemberDetailResponse.fromEntity(newBasicMember());

        given(memberService.getMemberInfo(any())).willReturn(memberDetailResponse);
        mockMvc.perform(get("/api/member"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.loginType").value("EMAIL"))
                .andExpect(jsonPath("$.data.nickname").value("nickNameTest"))
                .andExpect(jsonPath("$.data.profileImageUrl").value("naver.com"))
                .andExpect(jsonPath("$.data.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data.ageType").value("TEENAGER"))
                .andDo(print());

    }

    @Test
    @WithMockCustomUser
    @DisplayName("회원 정보 수정")
    public void updateMemberSuccess() throws Exception {

        MemberUpdateResponse memberUpdateResponse = MemberUpdateResponse.fromEntity(updateMember());
        MemberUpdateRequest memberUpdateRequest = memberUpdateRequest();

        given(memberService.updateMember(any(), any())).willReturn(memberUpdateResponse);
        mockMvc.perform(put("/api/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(memberUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickname").value("update my nickName"))
                .andExpect(jsonPath("$.data.profileImageUrl").value("updateNaver.com"))
                .andExpect(jsonPath("$.data.ageType").value("TEENAGER"))
                .andExpect(jsonPath("$.data.genderType").value("DEFAULT"))
                .andDo(print());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("비밀번호 수정")
    public void updatePasswordSuccess() throws Exception {
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest("changedPassword","newPassword");

        mockMvc.perform(put("/api/member/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(passwordUpdateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser()
    @DisplayName("여행 취향 수정")
    public void updateSurveySuccess() throws Exception {
        SurveyUpdateRequest surveyUpdateRequest = new SurveyUpdateRequest(survey());

        mockMvc.perform(put("/api/member/survey")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(surveyUpdateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("회원 탈퇴")
    public void deleteMemberSuccess() throws Exception {

        Member member = newMember();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        doNothing().when(memberService).deleteMember(member.getId(), request, response);

        mockMvc.perform(delete("/api/member"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockCustomUser
    @DisplayName("이미지 업로드")
    public void upLoadImageSuccess() throws Exception {

      //TODO:s3 모킹 코드 작성 예정
//
//        mockMvc.perform(post("/api/member")
//                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(objectMapper.writeValueAsBytes(any())))
//                .andExpect(status().isOk());

    }
}
