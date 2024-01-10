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
import org.tenten.tentenbe.domain.auth.dto.response.SignUpResponse;
import org.tenten.tentenbe.domain.auth.exception.DuplicateNicknameException;
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
    public SignUpResponse signUp(SignUpRequest signUpRequest, HttpServletResponse response) {
        if (memberRepository.existsByEmail(signUpRequest.email())) {
             throw new DuplicateNicknameException("이미 존재하는 이메일입니다.");
        }
        // TODO : 이메일 인증
        // 비밀번호 암호화 후 새로운 member 객체를 생성하여 데이터베이스에 저장(리턴값x)
        String encodedPassword = passwordEncoder.encode(signUpRequest.password());
        Member newMember = signUpRequest.toEntity(encodedPassword, EMAIL, ROLE_USER);

        memberRepository.save(newMember);
        return firstLogin(signUpRequest, response, newMember);
    }

    @Transactional
    public SignUpResponse firstLogin(SignUpRequest signUpRequest, HttpServletResponse response, Member newMember) {

        Authentication authenticate = getAuthenticate(signUpRequest.email(), signUpRequest.password());
        TokenInfoDTO tokenInfoDTO = getTokenInfoDTO(response, authenticate);


        RefreshToken refreshToken = RefreshToken.builder()
            .member(newMember)
            .token(tokenInfoDTO.getRefreshToken())
            .build();
        refreshTokenRepository.save(refreshToken);

        String nickname = "위플러" + (newMember.getId() + 321);
        newMember.updateNickname(nickname);

        return SignUpResponse.builder()
            .memberId(newMember.getId())
            .email(newMember.getEmail())
            .nickName(nickname)
            .tokenInfo(tokenInfoDTO.toTokenIssueDTO())
            .build();
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest, HttpServletResponse response) {

        Authentication authenticate = getAuthenticate(loginRequest.email(), loginRequest.password());
        TokenInfoDTO tokenInfoDTO = getTokenInfoDTO(response, authenticate);

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
        return getResponse(memberRepository.existsByNickname(nickname));
    }

    @Transactional(readOnly = true)
    public CheckResponse emailCheck(String email) {
        return getResponse(memberRepository.existsByEmail(email));
    }

    private CheckResponse getResponse(boolean exists) {
        return CheckResponse.builder().exists(exists).build();
    }

    private TokenInfoDTO getTokenInfoDTO(HttpServletResponse response, Authentication authenticate) {
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);
        log.info("로그인 API 중 토큰 생성 로직 실행");

        CookieUtil.storeRefreshTokenInCookie(response, tokenInfoDTO.getRefreshToken()); // 쿠키 심는 로직
        return tokenInfoDTO;
    }

    private Authentication getAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }
}