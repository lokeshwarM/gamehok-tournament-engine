package com.gamehok.tournament.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3.0 / Swagger UI configuration.
 * <p>
 * Accessible at: /swagger-ui.html and /v3/api-docs
 * JWT Bearer token authentication is pre-configured as the security scheme.
 * </p>
 */
@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH = "bearerAuth";

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    @Bean
    public OpenAPI tournamentEngineOpenAPI() {
        return new OpenAPI()
                .info(buildApiInfo())
                .servers(buildServers())
                .components(buildComponents())
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }

    private Info buildApiInfo() {
        return new Info()
                .title("Gamehok Tournament Engine API")
                .version("1.0.0")
                .description("""
                        Production-grade esports tournament orchestration platform.
                        
                        ## Supported Tournament Formats
                        - **Knockout**: Single/double elimination brackets
                        - **League**: Round-robin point-based competition
                        - **Hybrid**: Group stage + playoff brackets
                        - **Battle Royale**: Free-for-all with multi-participant matches
                        - **Swiss**: Swiss-system pairing
                        
                        ## Team Sizes
                        Flexible team configurations from 1v1 (SOLO) to 5v5 (SQUAD).
                        
                        ## Authentication
                        All endpoints require a valid JWT Bearer token. Obtain one via `/api/v1/auth/login`.
                        """)
                .contact(new Contact()
                        .name("Gamehok Engineering")
                        .email("engineering@gamehok.com")
                        .url("https://gamehok.com"))
                .license(new License()
                        .name("Proprietary")
                        .url("https://gamehok.com/terms"));
    }

    private List<Server> buildServers() {
        return List.of(
                new Server().url("http://localhost:8080").description("Local Development"),
                new Server().url("https://api-staging.gamehok.com").description("Staging"),
                new Server().url("https://api.gamehok.com").description("Production")
        );
    }

    private Components buildComponents() {
        return new Components()
                .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .name(BEARER_AUTH)
                        .description("JWT Bearer token. Format: `Bearer <token>`"));
    }
}
