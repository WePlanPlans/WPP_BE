package org.tenten.tentenbe.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
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
import org.tenten.tentenbe.global.response.GlobalDataResponse;
import org.tenten.tentenbe.global.s3.ImageUploadDto;
import org.tenten.tentenbe.global.s3.S3Uploader;

import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final S3Uploader s3Uploader;

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

}
