package org.tenten.tentenbe.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(servers = {@Server(url = "https://api.weplanplans.site", description = "Server")})
@Configuration
public class SwaggerConfig {
    String JWT_SCHEME_NAME = "Authorization";
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("v1-definition")
            .pathsToMatch("/**")
            .build();
    }
    @Bean
    public OpenAPI springShopOpenAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(JWT_SCHEME_NAME);

        // SecuritySchemes 등록
        Components components = new Components()
            .addSecuritySchemes(JWT_SCHEME_NAME, new SecurityScheme()
                .name(JWT_SCHEME_NAME)
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .bearerFormat("JWT")); // 토큰 형식을 지정하는 임의의 문자(Optional)

        return new OpenAPI()
            .info(new Info().title("야놀자 파이널 프로젝트")
                .description("파이널 프로젝트 api")
                .version("v1"))
            .addSecurityItem(securityRequirement)
            .components(components);
    }
}
