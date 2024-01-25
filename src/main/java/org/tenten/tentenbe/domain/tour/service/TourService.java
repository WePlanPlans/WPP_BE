package org.tenten.tentenbe.domain.tour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.liked.repository.LikedItemRepository;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.tour.dto.response.TourDetailResponse;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.tour.exception.TourException;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;
import org.tenten.tentenbe.global.common.enums.Category;
import org.tenten.tentenbe.global.common.enums.Region;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourService {
    private final TourItemRepository tourItemRepository;
    private final MemberRepository memberRepository;
    private final LikedItemRepository likedItemRepository;

    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> getTours(Long memberId, String regionName, Pageable pageable) {
        return getTourSimpleResponsePage(memberId, regionName, pageable);
    }

    private Page<TourSimpleResponse> getTourSimpleResponsePage(Long memberId, String regionName, Pageable pageable) {
        if (regionName == null) {
            return tourItemRepository.findPopularTourItems(memberId, pageable);
        }
        return tourItemRepository.findPopularTourItems(Region.fromName(regionName).getAreaCode(), memberId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> searchTours(
        Long memberId, String regionName, String categoryName, String searchWord, Pageable pageable
    ) {
        Optional<Member> member = Optional.ofNullable(memberId).map(memberRepository::getReferenceById);
        Long regionCode = findRegionCode(regionName);
        Long categoryCode = findCategoryCode(categoryName);

        Page<TourItem> tourItems =
            tourItemRepository.searchByRegionAndCategoryAndSearchWord(regionCode, categoryCode, searchWord, pageable);
        List<TourSimpleResponse> tourSimpleResponses = getTourSimpleResponses(member, tourItems);

        return new PageImpl<>(tourSimpleResponses, pageable, tourItems.getTotalElements());
    }

    private List<TourSimpleResponse> getTourSimpleResponses(Optional<Member> memberOptional, Page<TourItem> tourItems) {
        return tourItems.stream()
            .map(tourItem -> new TourSimpleResponse(
                tourItem.getId(),
                tourItem.getContentTypeId(),
                tourItem.getTitle(),
                tourItem.getReviews().stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0),
                tourItem.getReviewTotalCount(),
                tourItem.getLikedTotalCount(),
                memberOptional.map(member -> likedCheck(member, tourItem.getId())).orElse(false),
                tourItem.getSmallThumbnailUrl(),
                tourItem.getAddress(),
                tourItem.getLongitude(),
                tourItem.getLatitude()
            )).toList();
    }

    private Long findCategoryCode(String categoryName) {
        if (categoryName != null) {
            return Category.fromName(categoryName).getCode();
        }
        return null;
    }

    private Long findRegionCode(String regionName) {
        if (regionName != null) {
            return Region.fromName(regionName).getAreaCode();
        }
        return null;
    }

    private Boolean likedCheck(Member member, Long tourId) {
        return member.getLikedItems().stream()
            .map(LikedItem::getId)
            .toList()
            .contains(tourId);
    }

    @Transactional(readOnly = true)
    public TourDetailResponse getTourDetail(Long memberId, Long tourItemId) {
        TourItem tourItem = tourItemRepository.findById(tourItemId)
            .orElseThrow(() -> new TourException("해당 아이디로 존재하는 리뷰가 없습니다. tourItemId : " + tourItemId, NOT_FOUND));
        boolean liked = isLiked(memberId, tourItemId);

        return new TourDetailResponse(tourItem, liked, getFullAddress(tourItem.getAddress(), tourItem.getDetailedAddress()));
    }

    private boolean isLiked(Long memberId, Long tourItemId) {
        if (memberId != null) return likedItemRepository.existsByMemberIdAndTourItemId(memberId, tourItemId);
        return false;
    }

    private String getFullAddress(String address, String detailedAddress) {
        if (detailedAddress == null) return address;
        return address + " " + detailedAddress;
    }

}
