package com.k48.leonel.gestionblog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Métadonnées de la documentation OpenAPI (Swagger UI : /swagger-ui.html).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI blogOpenApi() {
        return new OpenAPI().info(new Info()
                .title("API de Gestion d'un Blog")
                .description("API REST permettant de publier des articles et d'ajouter des commentaires. "
                        + "Endpoints principaux : /api/articles et /api/articles/{articleId}/commentaires.")
                .version("1.0.0")
                .contact(new Contact().name("Leonel K48")));
    }
}
