package org.tenten.tentenbe.domain.member.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tenten.tentenbe.common.ServiceTest;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.liked.repository.LikedItemRepository;
import org.tenten.tentenbe.domain.member.dto.request.MemberUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.request.PasswordUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.request.SurveyUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.response.MemberDetailResponse;
import org.tenten.tentenbe.domain.member.dto.response.MemberUpdateResponse;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.review.dto.response.MemberReviewResponse;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.review.repository.ReviewRepository;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.tenten.tentenbe.common.fixture.AuthFixture.*;
import static org.tenten.tentenbe.common.fixture.MemberFixture.serviceLikedItem;
import static org.tenten.tentenbe.common.fixture.MemberFixture.updateSurvey;
import static org.tenten.tentenbe.common.fixture.ReviewFixture.review;

;
public class MemberServiceTest extends ServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private LikedItemRepository likedItemRepository;
    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("나의 관심 여행지 조회시 페이지 리스트로 반환한다. ")
    public void myFavoriteToursSuccess() throws Exception {
        Member member = newBasicMember();
        Pageable pageable = PageRequest.of(0, 10);
        List<LikedItem> likedItems = List.of(serviceLikedItem());
        Page<LikedItem> likedItemPage = new PageImpl<>(likedItems, pageable, likedItems.size());

        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(likedItemRepository.findByMember(any(), any())).willReturn(likedItemPage);

        Page<TourSimpleResponse> tourSimpleResponses = memberService.getTours(1L, pageable);

        assertNotNull(tourSimpleResponses);

        assertThat(tourSimpleResponses.getContent()).usingRecursiveAssertion().isEqualTo(
                likedItemPage.getContent().stream()
                        .map(likedItem -> TourSimpleResponse.fromEntity(likedItem.getTourItem())).toList()
        );
    }

    @Test
    @DisplayName("나의 리뷰 조회")
    public void getReviewsSuccess() throws Exception {

        Member member = newBasicMember();
        Pageable pageable = PageRequest.of(0, 10);
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Review> reviewList = List.of(review());
        Page<Review> reviewPage = new PageImpl<>(reviewList, pageable, reviewList.size());

        // getReview 에 멤버 조회가 빠져있음
        //  given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(reviewRepository.findReviewByCreatorId(any(), any())).willReturn(reviewPage);

        Page<MemberReviewResponse> memberReviewResponse = memberService.getReviews(1L, pageRequest);

        assertNotNull(memberReviewResponse);
        assertThat(memberReviewResponse.getContent()).usingRecursiveAssertion().isEqualTo(
                reviewPage.getContent().stream()
                        .map(review -> MemberReviewResponse.fromEntity(review)).toList()
        );

    }

    @Test
    @DisplayName("회원 정보 조회")
    public void getMemberInfoSuccess() throws Exception {

        Member member = newBasicMember();
        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        assertThat(memberService.getMemberInfo(member.getId())).isEqualTo(MemberDetailResponse.fromEntity(member));
    }

    @Test
    @DisplayName("회원 정보 수정")
    public void updateMemberSuccess() throws Exception {
        Member member = newBasicMember();

        MemberUpdateResponse memberUpdateResponse = MemberUpdateResponse.fromEntity(updateMember());
        MemberUpdateRequest memberUpdateRequest = memberUpdateRequest();

        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));

        assertThat(memberService.updateMember(member.getId(), memberUpdateRequest))
                .extracting("nickname", "profileImageUrl")
                .containsExactly("update my nickName", "updateNaver.com");

    }

    @Test
    @DisplayName("비밀번호 수정")
    public void updatePasswordSuccess() throws Exception {
        Member member = newBasicMember();
        PasswordUpdateRequest passwordUpdateRequest = new PasswordUpdateRequest("changedPassword","newPassword");

        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));
        given(!passwordEncoder.matches(passwordUpdateRequest.password(), member.getPassword())).willReturn(true);
        given(passwordEncoder.encode(any())).willReturn(passwordUpdateRequest.newPassword());

        memberService.updatePassword(member.getId(), passwordUpdateRequest);

        assertThat(member.getPassword()).isEqualTo(passwordUpdateRequest.newPassword());

    }

    @Test
    @DisplayName("여행 취향 수정")
    public void updateSurveySuccess() throws Exception {
        Member member = newBasicMember();
        SurveyUpdateRequest surveyUpdateRequest = new SurveyUpdateRequest(updateSurvey());

        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));

        memberService.updateSurvey(member.getId(), surveyUpdateRequest);

        assertThat(member.getSurvey()).extracting("planning", "activeHours", "accommodation", "food", "tripStyle")
                .containsExactly("철저하게", "아침형", "분위기", "음식", "액티비티");
    }

    @Test
    @DisplayName("회원 탈퇴")
    public void deleteMemberSuccess() throws Exception {

        Member member = newBasicMember();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(memberRepository.findById(member.getId())).willReturn(Optional.ofNullable(member));

        memberService.deleteMember(member.getId(), request, response);

        verify(memberRepository).delete(member);
    }

    @Test
    @DisplayName("이미지 업로드")
    public void upLoadImageSuccess() throws Exception {

        //TODO:s3 모킹
//


    }
}
