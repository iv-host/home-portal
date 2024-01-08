package org.ivcode.homeportal.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class SpringDocConfig(
    @Qualifier("version") private val version: String,
    @Value("\${security.oauth2.enabled:false}") private val isOAuthEnabled: Boolean,
    @Value("\${security.oauth2.client.authorization-url:#{null}}") private val authorizationUrl: String?,
    @Value("\${security.oauth2.client.token-url:#{null}}") private val tokenUrl: String?,
) {


    @Bean
    public fun createOpenAPI(): OpenAPI {
        return OpenAPI().apply {
            info(Info().title("home-portal").version(version))

            if(isOAuthEnabled) {
                components(Components()
                    .addSecuritySchemes(
                        "spring_oauth", SecurityScheme()
                            .type(SecurityScheme.Type.OAUTH2)
                            .`in`(SecurityScheme.In.HEADER)
                            .description("Oauth2 flow")
                            .flows(
                                OAuthFlows()
                                    .authorizationCode(
                                        OAuthFlow()
                                            .authorizationUrl(authorizationUrl)
                                            .tokenUrl(tokenUrl)
                                    )
                            )
                    )
                )

                security(listOf(SecurityRequirement().addList("spring_oauth")))
            }
        }
    }

}

