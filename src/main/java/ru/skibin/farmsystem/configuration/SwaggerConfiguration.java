package ru.skibin.farmsystem.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.skibin.farmsystem.util.SwaggerConfigurationConstraint.BARER_AUTH;
import static ru.skibin.farmsystem.util.SwaggerConfigurationConstraint.BARER_FORMAT;
import static ru.skibin.farmsystem.util.SwaggerConfigurationConstraint.BARER_SCHEME;
import static ru.skibin.farmsystem.util.SwaggerConfigurationConstraint.CONTACT_EMAIL;
import static ru.skibin.farmsystem.util.SwaggerConfigurationConstraint.CONTACT_NAME;
import static ru.skibin.farmsystem.util.SwaggerConfigurationConstraint.CONTACT_URL;
import static ru.skibin.farmsystem.util.SwaggerConfigurationConstraint.DESCRIPTION;
import static ru.skibin.farmsystem.util.SwaggerConfigurationConstraint.TITLE;
import static ru.skibin.farmsystem.util.SwaggerConfigurationConstraint.VERSION;

@Configuration
public class SwaggerConfiguration {
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat(BARER_FORMAT)
                .scheme(BARER_SCHEME);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList(BARER_AUTH))
                .components(new Components().addSecuritySchemes
                        (BARER_AUTH, createAPIKeyScheme()))
                .info(new Info().title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION).contact(new Contact().name(CONTACT_NAME)
                                .email(CONTACT_EMAIL)
                                .url(CONTACT_URL)
                        )
                );
    }
}
