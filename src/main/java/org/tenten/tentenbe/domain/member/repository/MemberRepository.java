package org.tenten.tentenbe.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tenten.tentenbe.domain.member.model.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}
