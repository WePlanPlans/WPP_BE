package org.tenten.tentenbe.domain.liked.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.tenten.tentenbe.common.ServiceTest;
import org.tenten.tentenbe.domain.liked.repository.LikedItemRepository;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.tour.repository.TourItemRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.tenten.tentenbe.common.fixture.AuthFixture.newBasicMember;
import static org.tenten.tentenbe.common.fixture.MemberFixture.likedItem;
import static org.tenten.tentenbe.common.fixture.MemberFixture.tourItem;

public class LikesServiceTest extends ServiceTest {
    @InjectMocks
    private LikedService likedService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TourItemRepository tourItemRepository;
    @Mock
    private LikedItemRepository likedItemRepository;



    @Test
    public void likeTourSuccess() {
        given(memberRepository.findById(any())).willReturn(Optional.of(newBasicMember()));
        given(tourItemRepository.findById(any())).willReturn(Optional.of(tourItem()));
        given(likedItemRepository.findByMemberAndTourItem(any(), any())).willReturn(Optional.empty());

        assertDoesNotThrow(() -> likedService.likeTour(tourItem().getId(), newBasicMember().getId()));

        // 메서드 검증
        verify(memberRepository, times(1)).findById(newBasicMember().getId());
        verify(tourItemRepository, times(1)).findById(tourItem().getId());
        verify(likedItemRepository, times(1)).findByMemberAndTourItem(any(), any());
        verify(likedItemRepository, times(1)).save(any());
    }

    @Test
    public void cancelLikeTourSuccess() {

        given(memberRepository.findById(any())).willReturn(Optional.of(newBasicMember()));
        given(tourItemRepository.findById(any())).willReturn(Optional.of(tourItem()));
        given(likedItemRepository.findByMemberAndTourItem(any(), any())).willReturn(Optional.of(likedItem()));

        // 테스트 수행
        assertDoesNotThrow(() -> likedService.cancelLikeTour(tourItem().getId(), newBasicMember().getId()));

        // 해당 메서드들이 호출되었는지 검증
        verify(memberRepository, times(1)).findById(newBasicMember().getId());
        verify(tourItemRepository, times(1)).findById(tourItem().getId());
        verify(likedItemRepository, times(1)).findByMemberAndTourItem(any(), any());
        verify(likedItemRepository, times(1)).delete(any());
    }
}
