package com.eventos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do OpenAPI (Swagger).
 * Define informações da API e suporte a autenticação JWT na documentação.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Eventos")
                        .description("API REST para cadastro de eventos, usuários e inscrições. " +
                                "Utiliza autenticação JWT. Faça login em POST /auth/login e use o token " +
                                "no botão 'Authorize' para testar endpoints protegidos.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Eventos API")
                                .email("contato@eventos.com")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat("JWT")
                                        .scheme("bearer")
                                        .description("Token JWT obtido no endpoint /auth/login")));
    }
}
