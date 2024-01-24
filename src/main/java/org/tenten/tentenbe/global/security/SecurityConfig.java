package org.tenten.tentenbe.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.tenten.tentenbe.global.security.filter.JwtFilter;
import org.tenten.tentenbe.global.security.jwt.JwtAuthenticationEntryPoint;
import org.tenten.tentenbe.global.security.oauth.OAuth2UserService;
import org.tenten.tentenbe.global.security.oauth.OAuthLoginSuccessHandler;

import java.util.List;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final OAuth2UserService oAuth2UserService;
    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;

    private static final String[] PERMIT_PATHS = {
        "/api/auth/**", "/swagger-ui/**", "/api/region/**", "/api/category", "/api/tours/**", "/",
        "/api-docs/**", "/api/trips/**", "/api/reviews/**", "/api/comments/**", "/login/kakao/**"
    };

    private static final String[] ALLOW_ORIGINS = {
        "http://localhost:8080",
        "http://localhost:5173",
        "https://api.weplanplans.site",
        "https://weplanplans.vercel.app",
        "https://dev-weplanplans.vercel.app"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((request) -> request
                .requestMatchers("/api/auth/logout").authenticated()
                .requestMatchers(OPTIONS, "**").permitAll()
                .requestMatchers(PERMIT_PATHS).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(exceptionHandling ->
            exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        // OAuth2 로그인
        http.oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint( //OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당한다.
                userInfoEndpoint -> userInfoEndpoint.userService(oAuth2UserService)) //userService 에 소셜 로그인 성공 시 진행할 OAuth2UserService 인터페이스의 구현체를 등록
            .successHandler(oAuthLoginSuccessHandler)
        );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(ALLOW_ORIGINS));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.addExposedHeader("Authorization");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
    public WebSecurityCustomizer configureH2ConsoleEnable() {
        return web -> web.ignoring()
            .requestMatchers(PathRequest.toH2Console());
    }

}