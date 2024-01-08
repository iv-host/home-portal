package org.ivcode.homeportal.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.ivcode.homeportal.api.OAuth2TokenApi
import org.ivcode.homeportal.controllers.PATH_OAUTH2
import org.ivcode.homeportal.controllers.PATH_OAUTH2_REDIRECT
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


@Configuration
@ConditionalOnProperty(value = ["security.oauth2.enabled"], havingValue = "true", matchIfMissing = false)
public class OAuth2ClientConfig {

    @Bean("oauth2.redirect-path")
    public fun createRedirectPath(): String {
        return "${PATH_OAUTH2}${PATH_OAUTH2_REDIRECT}"
    }

    @Bean
    public fun createOAuth2TokenApi (objectMapper: ObjectMapper, @Value("\${security.oauth2.client.token-url}") tokenUrl: String): OAuth2TokenApi {
        val url = if(tokenUrl.endsWith('/')) {
            tokenUrl
        } else {
            "${tokenUrl}/"
        }
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

        return retrofit.create(OAuth2TokenApi::class.java)
    }
}