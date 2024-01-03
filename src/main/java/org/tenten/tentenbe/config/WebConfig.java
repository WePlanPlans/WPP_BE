package org.tenten.tentenbe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173", "https://api.weplanplans.site", "https://weplanplans.vercel.app")
            .allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD", "OPTIONS", "PUT")
            .allowCredentials(true)
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            .maxAge(3000);
    }
}