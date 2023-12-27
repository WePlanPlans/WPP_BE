package org.tenten.tentenbe.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    servers = {
        @Server(url = "https://api.weplanplans.site", description = "Server")
    },
    info = @Info(
        title = "야놀자 파이널 프로젝트",
        description = "파이널 프로젝트 api",
        version = "v1"
    )
)
@Configuration
public class SwaggerConfig {
}
