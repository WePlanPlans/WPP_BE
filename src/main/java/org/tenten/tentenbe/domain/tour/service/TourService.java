package org.tenten.tentenbe.domain.tour.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.member.model.LikedItem;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.tour.dto.response.TourDetailResponse;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.tour.exception.TourException;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;
import org.tenten.tentenbe.global.common.enums.Region;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourItemRepository tourItemRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> getTours(Long memberId, String region, Pageable pageable) {
        Member member = memberRepository.getReferenceById(memberId);
        List<TourItem> tourItems;
        if(region == null) {
            tourItems = tourItemRepository.findAll();
        } else {
            tourItems = tourItemRepository.findByAreaCode(Region.fromName(region).getAreaCode());
        }

        List<TourSimpleResponse> tourSimpleResponses = tourItems.stream()
            .map(tourItem -> new TourSimpleResponse(
                tourItem.getId(),
                tourItem.getTitle(),
                tourItem.getReviews().stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0),
                (long) tourItem.getReviews().size(),
                (long) tourItem.getTripLikedItems().size(),
                likedCheck(member, tourItem.getId()),
                tourItem.getSmallThumbnailUrl()
            )).collect(Collectors.toList());

        return new PageImpl<>(tourSimpleResponses, pageable, tourItems.size());
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
