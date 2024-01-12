package org.tenten.tentenbe.domain.auth.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.tenten.tentenbe.domain.fixture.AuthFixture;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.tenten.tentenbe.global.common.enums.UserAuthority.ROLE_USER;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("회원 저장 성공")
    public void memberSave() {

        Member member = memberRepository.save(AuthFixture.newMember());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo("test@gmail.com");
        assertThat(member.getUserAuthority()).isEqualTo(ROLE_USER);
    }

    @Test
    @DisplayName("회원 아이디로 회원 정보 조회 성공")
    public void memberFindById() {

        Member saveMember = memberRepository.save(AuthFixture.newMember());
        Optional<Member> findMember = memberRepository.findById(saveMember.getId());

        assertThat(findMember).isNotNull();
        assertThat(saveMember.getId()).isEqualTo(findMember.get().getId());
    }

    @Test
    @DisplayName("존재하는 이메일 확인")
    public void existsByEmail() {

        Member saveMember = memberRepository.save(AuthFixture.newMember());
        Boolean checkEmail = memberRepository.existsByEmail(saveMember.getEmail());

        assertThat(checkEmail).isEqualTo(true);
    }

    @Test
    @DisplayName("존재하는 닉네임 확인")
    public void existsByNickname() {

        Member saveMember = memberRepository.save(AuthFixture.newMember());
        Boolean checkNickName = memberRepository.existsByNickname(saveMember.getNickname());

        assertThat(checkNickName).isEqualTo(true);
    }

}
