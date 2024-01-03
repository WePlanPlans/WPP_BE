package org.tenten.tentenbe.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenbe.domain.auth.dto.request.LoginRequest;
import org.tenten.tentenbe.domain.auth.dto.request.SignUpRequest;
import org.tenten.tentenbe.domain.auth.dto.response.CheckResponse;
import org.tenten.tentenbe.domain.auth.dto.response.LoginResponse;
import org.tenten.tentenbe.domain.auth.dto.response.MemberDto;
import org.tenten.tentenbe.domain.member.model.Member;
import org.tenten.tentenbe.domain.member.repository.MemberRepository;
import org.tenten.tentenbe.domain.token.dto.TokenDTO.TokenInfoDTO;
import org.tenten.tentenbe.global.security.jwt.JwtTokenProvider;
import org.tenten.tentenbe.global.security.jwt.model.RefreshToken;
import org.tenten.tentenbe.global.security.jwt.repository.RefreshTokenRepository;
import org.tenten.tentenbe.global.util.CookieUtil;

import static org.tenten.tentenbe.global.common.enums.LoginType.EMAIL;
import static org.tenten.tentenbe.global.common.enums.UserAuthority.ROLE_USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
//        //  TODO : 커스텀 예외처리 -> 이미 존재하는 유저(이메일) 일때
//        if (memberRepository.existsByEmail(signUpRequest.email())) {
//             throw new DuplicateNicknameException("이미 존재하는 이메일입니다.");
//        }
//        // TODO : 커스텀 예외처리 -> 이미 존재하는 닉네임 일때
//        if (memberRepository.existsByNickname(signUpRequest.nickname())) {
//            throw new DuplicateNicknameException("이미 존재하는 닉네임입니다.");
//        }
        // TODO : 이메일 인증
        // 비밀번호 암호화 후 새로운 member 객체를 생성하여 데이터베이스에 저장(리턴값x)
        String encodedPassword = passwordEncoder.encode(signUpRequest.password());
        Member newMember = signUpRequest.toEntity(encodedPassword, EMAIL, ROLE_USER);
        RefreshToken refreshToken = RefreshToken.builder()
            .member(newMember)
            .build();
        memberRepository.save(newMember);
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);
        log.info("로그인 API 중 토큰 생성 로직 실행");
        // 쿠키 심는 로직
        CookieUtil.storeRefreshTokenInCookie(response, tokenInfoDTO.getRefreshToken());

        String memberId = authenticate.getName();
        Member member = memberRepository.findById(Long.parseLong(memberId)).orElseThrow(RuntimeException::new);

        String refreshToken = tokenInfoDTO.getRefreshToken();
        member.getRefreshToken().updateToken(refreshToken);

        return LoginResponse.builder()
            .memberDto(MemberDto.fromEntity(member))
            .tokenInfo(tokenInfoDTO.toTokenIssueDTO())
            .build();
    }

    @Transactional
    public LoginResponse loginKakao(Long memberId, LoginRequest loginRequest) {
        return null;
    }


    @Transactional(readOnly = true)
    public CheckResponse nicknameCheck(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            return CheckResponse.builder().exists(false).build(); // 닉네임 중복 시 false 반환
        } else {
            return CheckResponse.builder().exists(true).build(); // 중복 아닐 시 true 반환
        }
    }

    @Transactional(readOnly = true)
    public CheckResponse emailCheck(String email) {
        if (memberRepository.existsByEmail(email)) {
            return CheckResponse.builder().exists(false).build(); // 이메일 중복 시 false 반환
        } else {
            return CheckResponse.builder().exists(true).build(); // 중복 아닐 시 true 반환
        }
    }
}