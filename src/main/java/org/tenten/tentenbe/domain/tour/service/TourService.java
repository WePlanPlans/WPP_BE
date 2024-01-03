package org.tenten.tentenbe.domain.tour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
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

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourService {
    private final TourItemRepository tourItemRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> getTours(Long memberId, String regionName, Pageable pageable) {
        Member member = memberRepository.getReferenceById(memberId);

        return getTourSimpleResponsePage(memberId, regionName, pageable);
    }

    private Page<TourSimpleResponse> getTourSimpleResponsePage(Long memberId, String regionName, Pageable pageable) {
        if (regionName == null) {
            return tourItemRepository.findPopularTourItems(memberId, pageable);
        }
        return tourItemRepository.findPopularTourItems(Region.fromName(regionName).getAreaCode(), memberId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> searchTours(Long memberId, String regionName, String categoryName, String searchWord, Pageable pageable) {
        Member member = memberRepository.getReferenceById(memberId);
        Region region = findRegion(regionName);
        Category category = findCategory(categoryName);

        Specification<TourItem> spec = Specification.where(regionIs(region))
            .and(categoryIs(category))
            .and(nameOrAddressContains(searchWord));
        List<TourItem> tourItems = tourItemRepository.findAll(spec);

        List<TourSimpleResponse> tourSimpleResponses = getTourSimpleResponses(member, tourItems, pageable);

        return new PageImpl<>(tourSimpleResponses, pageable, tourItems.size());
    }

    private Specification<TourItem> regionIs(Region region) {
        return (root, query, cb) -> {
            if(region == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("areaCode"), region.getAreaCode());
        };
    }

    private Specification<TourItem> categoryIs(Category category) {
        return (root, query, cb) -> {
            if (category == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("contentTypeId"), category.getCode());
        };
    }

    private Specification<TourItem> nameOrAddressContains(String searchWord) {
        return (root, query, cb) -> cb.or(
            cb.like(root.get("title"), "%" + searchWord + "%"),
            cb.like(root.get("address"), "%" + searchWord + "%"),
            cb.like(root.get("detailedAddress"), "%" + searchWord + "%")
        );
    }

    @Transactional(readOnly = true)
    public TourDetailResponse getTourDetail(Long memberId, Long tourItemId) {
        TourItem tourItem = tourItemRepository.findById(tourItemId)
            .orElseThrow(() -> new TourException("해당 아이디로 존재하는 리뷰가 없습니다. tourItemId : " + tourItemId, NOT_FOUND));
        Member member = memberRepository.getReferenceById(memberId);

        Boolean liked = likedCheck(member, tourItem.getId());

        return new TourDetailResponse(tourItem, liked, getFullAddress(tourItem.getAddress(), tourItem.getDetailedAddress()));
    }

    private String getFullAddress(String address, String detailedAddress) {
        if (detailedAddress == null) {
            return address;
        }
        return address + " " + detailedAddress;
    }

    private List<TourSimpleResponse> getTourSimpleResponses(Member member, List<TourItem> tourItems, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), tourItems.size());

        return tourItems.stream()
            .map(tourItem -> new TourSimpleResponse(
                tourItem.getId(),
                tourItem.getContentTypeId(),
                tourItem.getTitle(),
                tourItem.getReviews().stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0),
                (long) tourItem.getReviews().size(),
                (long) tourItem.getTripLikedItems().size(),
                likedCheck(member, tourItem.getId()),
                tourItem.getSmallThumbnailUrl(),
                tourItem.getAddress(),
                tourItem.getLongitude(),
                tourItem.getLatitude()
            )).toList()
            .subList(start, end);
    }

    private Category findCategory(String categoryName) {
        if (categoryName != null) {
            return Category.fromName(categoryName);
        }
        return null;
    }

    private Region findRegion(String regionName) {
        if (regionName != null) {
            return Region.fromName(regionName);
        }
        return null;
    }

    private Boolean likedCheck(Member member, Long tourId) {
        return member.getLikedItems().stream()
            .map(LikedItem::getId)
            .toList()
            .contains(tourId);
    }

}
