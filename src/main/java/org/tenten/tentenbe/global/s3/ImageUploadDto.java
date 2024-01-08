package org.tenten.tentenbe.global.s3;

import lombok.Builder;

@Builder
public record ImageUploadDto(
    String imageUrl,
    String message

) {
}