package br.com.moonspace.cdc.router.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(contact = @Contact(name = "Moonspace",
                email = "contato@moonspace.com.br"),
                title = "Change Data Capture - Connector",
                termsOfService = "www.moonspace.com.br/terms-and-condition",
                description = "${api.description}",
                version = "${api.version}"),
        servers = {
                @Server(url = "http://localhost:8081${server.servlet.context-path}", description = "Development"),
                @Server(url = "${api.url.homologation}${server.servlet.context-path}", description = "Homologation"),
                @Server(url = "${api.url.production}", description = "Production")
        }
)
public class OpenAPIConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {
        final var securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .description("Provide the JWT token.")
                                .bearerFormat("JWT")));
    }

}
