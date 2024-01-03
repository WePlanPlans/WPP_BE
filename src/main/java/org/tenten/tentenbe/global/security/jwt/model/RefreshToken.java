package org.tenten.tentenbe.global.security.jwt.model;

import jakarta.persistence.*;
import lombok.*;
import org.tenten.tentenbe.domain.member.model.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refreshTokenId")
    private Long id;

    @Column(unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    public void updateToken(String token) {
        this.token = token;
    }

}