package org.tenten.tentenbe.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(

    @Schema(defaultValue = "example@mail.com")
    String email,

    @Schema(defaultValue = "as@#SD23/&DFd%fs@a1")
    String password
) {
}
