package org.tenten.tentenbe.domain.liked.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.liked.exception.ConflictException;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.liked.repository.LikedItemRepository;
import org.tenten.tentenbe.domain.member.exception.UserNotFoundException;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.tour.exception.TourNotFoundException;
import org.tenten.tentenbe.domain.tour.model.TourItem;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;

@Service
@RequiredArgsConstructor
public class LikedService {
    private final LikedItemRepository likedItemRepository;
    private final MemberRepository memberRepository;
    private final TourItemRepository tourItemRepository;

    @Transactional
    public void likeTour(Long tourId, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UserNotFoundException("해당 아이디로 존재하는 유저가 없습니다."));
        TourItem tourItem = tourItemRepository.findById(tourId)
            .orElseThrow(() -> new TourNotFoundException("해당 아이디로 존재하는 여행지가 없습니다."));

        if (likedItemRepository.findByMemberAndTourItem(member, tourItem).isPresent()){
            throw new ConflictException("이미 관심 등록된 여행지입니다.");
        }

        LikedItem likedItem = LikedItem.builder()
            .member(member)
            .tourItem(tourItem)
            .build();
        tourItem.increaseLikedCount();
        likedItemRepository.save(likedItem);

    }

    @Transactional
    public void cancelLikeTour(Long tourId, Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new UserNotFoundException("해당 아이디로 존재하는 유저가 없습니다."));
        TourItem tourItem = tourItemRepository.findById(tourId)
            .orElseThrow(() -> new TourNotFoundException("해당 아이디로 존재하는 여행지가 없습니다."));

        LikedItem likedItem = likedItemRepository.findByMemberAndTourItem(member, tourItem)
            .orElseThrow(() -> new ConflictException("관심 등록되지 않은 여행지입니다."));
        likedItemRepository.delete(likedItem);
        tourItem.decreaseLikedCount();
    }

}
