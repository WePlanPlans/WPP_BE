package org.tenten.tentenbe.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.auth.dto.request.LoginRequest;
import org.tenten.tentenbe.domain.auth.dto.request.SignUpRequest;
import org.tenten.tentenbe.domain.auth.dto.response.LoginResponse;
import org.tenten.tentenbe.domain.auth.exception.MemberAlreadyExistException;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;

import static org.tenten.tentenbe.global.common.enums.LoginType.EMAIL;
import static org.tenten.tentenbe.global.common.enums.UserAuthority.ROLE_USER;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        //  TODO : 커스텀 예외처리 -> 이미 존재하는 유저(이메일) 일때
        if (memberRepository.existsByEmail(signUpRequest.email())) {
             throw new MemberAlreadyExistException();
        }
        // TODO : 커스텀 예외처리 -> 이미 존재하는 닉네임 일때
        if (memberRepository.existsByNickname(signUpRequest.nickname())) {
            throw new MemberAlreadyExistException();
        }
        // TODO : 이메일 인증
        // 비밀번호 암호화 후 새로운 member 객체를 생성하여 데이터베이스에 저장(리턴값x)
        String encodedPassword = passwordEncoder.encode(signUpRequest.password());
        Member newMember = signUpRequest.toEntity(encodedPassword, EMAIL, ROLE_USER);
        memberRepository.save(newMember);
    };

    @Transactional
    public LoginResponse login(Long memberId, LoginRequest loginRequest) {
        return null;
    }
    @Transactional
    public LoginResponse loginKakao(Long memberId, LoginRequest loginRequest) {
        return null;
    }

    @Transactional
    public void logout(Long memberId) {
    }

}