package org.tenten.tentenbe.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.global.common.enums.LoginType;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByEmailAndLoginType(String email, LoginType loginType);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndLoginType(String email, LoginType loginType);
}
