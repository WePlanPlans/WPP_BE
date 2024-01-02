package org.tenten.tentenbe.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public record LoginRequest(
    @NotNull
    @Email
    @Schema(description = "로그인할 이메일", defaultValue = "example@mail.com")
    String email,

    @NotNull
    @Size(min = 8, max = 20)
    @Schema(description = "비밀번호", defaultValue = "as@#SD23/&DFd%fs@a1")
    String password
) {
    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
