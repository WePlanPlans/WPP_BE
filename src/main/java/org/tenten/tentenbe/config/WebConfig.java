package org.tenten.tentenbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] ALLOW_ORIGINS = {
        "http://localhost:8080",
        "http://localhost:5173",
        "https://api.weplanplans.site",
        "https://weplanplans.vercel.app",
        "https://dev-weplanplans.vercel.app"
    };

    private static final String[] ALLOW_METHODS = {
        "GET", "POST", "PATCH", "DELETE", "HEAD", "OPTIONS", "PUT"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(ALLOW_ORIGINS)
            .allowedMethods(ALLOW_METHODS)
            .allowCredentials(true)
            .maxAge(3000);
    }
}