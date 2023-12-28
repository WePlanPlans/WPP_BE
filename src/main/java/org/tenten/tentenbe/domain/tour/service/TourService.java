package org.tenten.tentenbe.domain.tour.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.member.model.LikedItem;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.tour.dto.response.TourDetailResponse;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.tour.exception.TourException;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourItemRepository tourItemRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> getTours(String region) {


        return null;
    }

    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> searchTours(String region, String type, String keyword) {
        return null;
    }

    @Transactional(readOnly = true)
    public TourDetailResponse getTourDetail(Long memberId, Long tourItemId) {
        TourItem tourItem = tourItemRepository.findById(tourItemId)
            .orElseThrow(() -> new TourException("해당 아이디로 존재하는 리뷰가 없습니다. tourItemId : " + tourItemId, NOT_FOUND));
        Member member = memberRepository.getReferenceById(memberId);

        Boolean liked = likedCheck(member, tourItem.getId());

        return new TourDetailResponse(
            tourItem,
            liked
        );
    }

    private Boolean likedCheck(Member member, Long tourId) {
        return member.getLikedItems().stream()
            .map(LikedItem::getId)
            .collect(Collectors.toSet())
            .contains(tourId);
    }

}
