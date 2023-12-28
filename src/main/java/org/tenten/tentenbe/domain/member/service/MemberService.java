package org.tenten.tentenbe.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.member.dto.request.MemberUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.response.MemberResponse;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.review.dto.response.ReviewInfo;
import org.tenten.tentenbe.domain.review.dto.response.ReviewResponse;
import org.tenten.tentenbe.domain.tour.dto.response.TourResponse;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<TripSimpleResponse> getTrips(Long memberId) {
        return null;
    }

    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> getTours(Long memberId) {
        return null;
    }

    @Transactional(readOnly = true)
    public Page<ReviewInfo> getReviews(Long memberId) {
        return null;
    }

    @Transactional
    public void deleteTour(Long memberId, Long commentId) {
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(Long memberId) {
        return null;
    }

    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        return null;
    }

    @Transactional
    public void deleteMember(Long memberId) {
    }
}
