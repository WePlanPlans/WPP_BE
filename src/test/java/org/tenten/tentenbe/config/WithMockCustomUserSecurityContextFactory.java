package org.tenten.tentenbe.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.tenten.tentenbe.domain.auth.dto.request.SignUpRequest;
import org.tenten.tentenbe.domain.member.model.Member;

import java.util.Arrays;

import static org.tenten.tentenbe.global.common.enums.LoginType.EMAIL;
import static org.tenten.tentenbe.global.common.enums.UserAuthority.ROLE_USER;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Member principal = new SignUpRequest(customUser.customUserName(),customUser.customUserPassword()).toEntity(customUser.customUserPassword(), EMAIL, ROLE_USER);
        Authentication auth = new UsernamePasswordAuthenticationToken(customUser.customUserId(), principal.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        context.setAuthentication(auth);
        return context;
    }
}


