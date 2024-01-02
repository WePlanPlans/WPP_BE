package org.tenten.tentenbe.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.member.dto.request.MemberUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.response.MemberDetailResponse;
import org.tenten.tentenbe.domain.member.dto.response.MemberResponse;
import org.tenten.tentenbe.domain.member.exception.UserNotFoundException;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.review.dto.response.MemberReviewResponse;
import org.tenten.tentenbe.domain.review.dto.response.ReviewInfo;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.review.repository.ReviewRepository;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Page<TripSimpleResponse> getTrips(Long memberId, Pageable pageable) {

        return null;
    }

    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> getTours(Long memberId, Pageable pageable) {
        return null;
    }

    @Transactional(readOnly = true)
    public Page<MemberReviewResponse> getReviews(Long memberId, PageRequest pageRequest) {
        Page<Review> reviewPage = reviewRepository.findReviewByCreatorId(memberId, pageRequest);
        return reviewPage.map(MemberReviewResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public MemberDetailResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UserNotFoundException("해당 아이디로 존재하는 유저가 없습니다."));
        return MemberDetailResponse.fromEntity(member);
    }

    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        return null;
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UserNotFoundException("해당 아이디로 존재하는 유저가 없습니다."));
        memberRepository.delete(member); // TODO: 쿠키 삭제 필요성 검토
    }
}
