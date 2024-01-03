package org.tenten.tentenbe.global.s3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.tenten.tentenbe.global.response.GlobalDataResponse;

import static org.tenten.tentenbe.global.common.constant.ResponseConstant.SUCCESS;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/images")
@Tag(name = "이미지 관련 API", description = "S3 이미지 업로드 API 입니다.")
@Slf4j
public class S3Controller {
    private final S3Uploader s3Uploader;

    @Operation(
            summary = "이미지 파일 업로드 API",
            description = "MultipartFile 형태의 이미지 파일을 'images'라는 키로 form-data 형태로 전송해주세요. 이 API는 전송된 이미지를 S3에 저장하고, 저장된 이미지의 URL을 반환합니다."
    )
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlobalDataResponse<ImageUploadDto>> uploadImage(@RequestParam("images") MultipartFile multipartFile) throws BadRequestException {
        try {
            String uploadedUrl = s3Uploader.uploadFiles(multipartFile, "static");
            ImageUploadDto imageUpload = ImageUploadDto.builder()
                .imageUrl(uploadedUrl)
                .message("이미지 업로드에 성공했습니다.")
                .build();
            return ResponseEntity.ok(GlobalDataResponse.ok(SUCCESS, imageUpload));
        } catch (Exception e) {
            throw new BadRequestException("잘못된 요청입니다.");
        }
    }


}
