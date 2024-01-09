package org.tenten.tentenbe.global.security.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.global.security.jwt.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String refreshToken);
    Optional<RefreshToken> findByMember_Id(Long id);
}

