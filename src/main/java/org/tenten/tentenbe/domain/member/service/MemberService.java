package org.tenten.tentenbe.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tenten.tentenbe.domain.liked.model.LikedItem;
import org.tenten.tentenbe.domain.liked.repository.LikedItemRepository;
import org.tenten.tentenbe.domain.member.dto.request.MemberUpdateRequest;
import org.tenten.tentenbe.domain.member.dto.response.MemberDetailResponse;
import org.tenten.tentenbe.domain.member.dto.response.MemberUpdateResponse;
import org.tenten.tentenbe.domain.member.exception.UserNotFoundException;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.review.dto.response.MemberReviewResponse;
import org.tenten.tentenbe.domain.review.model.Review;
import org.tenten.tentenbe.domain.review.repository.ReviewRepository;
import org.tenten.tentenbe.domain.tour.dto.response.TourSimpleResponse;
import org.tenten.tentenbe.domain.trip.dto.response.TripSimpleResponse;
import org.tenten.tentenbe.global.common.enums.LoginType;
import org.tenten.tentenbe.global.s3.ImageUploadDto;
import org.tenten.tentenbe.global.s3.S3Uploader;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final S3Uploader s3Uploader;
    private final PasswordEncoder passwordEncoder;
    private final LikedItemRepository likedItemRepository;

    @Transactional(readOnly = true)
    public Page<TripSimpleResponse> getTrips(Long memberId, Pageable pageable) {

        return null;
    }

    @Transactional(readOnly = true)
    public Page<TourSimpleResponse> getTours(Long memberId, Pageable pageable) {
        Member member = getMember(memberId);
        Page<LikedItem> likedItems = likedItemRepository.findByMember(member, pageable);

        List<TourSimpleResponse> tourSimpleResponses = likedItems
            .stream()
            .map(likedItem -> TourSimpleResponse.fromEntity(likedItem.getTourItem()))
            .toList();

        return new PageImpl<>(tourSimpleResponses, pageable, likedItems.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<MemberReviewResponse> getReviews(Long memberId, PageRequest pageRequest) {
        Page<Review> reviewPage = reviewRepository.findReviewByCreatorId(memberId, pageRequest);
        return reviewPage.map(MemberReviewResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public MemberDetailResponse getMemberInfo(Long memberId) {
        Member member = getMember(memberId);
        return MemberDetailResponse.fromEntity(member);
    }

    @Transactional
    public MemberUpdateResponse updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest) {
        Member member = getMember(memberId);
        if (member.getLoginType() == LoginType.EMAIL) { // 이메일 로그인 유저일 경우
            member.updateMember(memberUpdateRequest);

            String encodedPassword = passwordEncoder.encode(memberUpdateRequest.password());
            member.updatePassword(encodedPassword);
        } else { // 카카오 로그인 유저일 경우
            member.updateMember(memberUpdateRequest);
        }
        return MemberUpdateResponse.fromEntity(member);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = getMember(memberId);
        memberRepository.delete(member); // TODO: 쿠키 삭제 필요성 검토
    }

    public ImageUploadDto uploadImage(MultipartFile multipartFile) throws BadRequestException {
        try {
            String uploadedUrl = s3Uploader.uploadFiles(multipartFile, "static");
            return ImageUploadDto.builder()
                .imageUrl(uploadedUrl)
                .message("이미지 업로드에 성공했습니다.")
                .build();
        } catch (Exception e) {
            throw new BadRequestException("잘못된 요청입니다.");
        }
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new UserNotFoundException("해당 아이디로 존재하는 유저가 없습니다."));
    }

}