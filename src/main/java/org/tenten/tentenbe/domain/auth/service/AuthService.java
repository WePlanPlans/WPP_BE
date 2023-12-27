package org.tenten.tentenbe.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.auth.dto.request.LoginRequest;
import org.tenten.tentenbe.domain.auth.dto.request.SignUpRequest;
import org.tenten.tentenbe.domain.auth.dto.response.LoginResponse;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(Long memberId, SignUpRequest signUpRequest) {};
    @Transactional
    public LoginResponse login(Long memberId, LoginRequest loginRequest) {
        return null;
    }
    @Transactional
    public LoginResponse loginKakao(Long memberId, LoginRequest loginRequest) {
        return null;
    }
    @Transactional
    public void logout(Long memberId) {}

}