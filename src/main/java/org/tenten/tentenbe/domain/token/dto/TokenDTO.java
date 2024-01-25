package org.tenten.tentenbe.domain.token.dto;

import lombok.*;

public class TokenDTO {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TokenInfoDTO {
        private String grantType;
        private String accessToken;
        private Long accessTokenExpiresIn;
        private String refreshToken;

        public TokenIssueDTO toTokenIssueDTO() {
            return TokenIssueDTO.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .grantType(grantType).build();
        }

    }

    @Getter
    @AllArgsConstructor
    @Setter
    @NoArgsConstructor
    @Builder
    public static class ReissueTokenDto {
        private String accessToken;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TokenIssueDTO {
        private String accessToken;
        private String grantType;
        private Long accessTokenExpiresIn;

    }

}
