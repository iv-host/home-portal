package org.ivcode.homeportal.config

import org.ivcode.homeportal.utils.claimquery.JwtClaimQuery
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

const val ADMIN_AUTHORITY = "ADMIN"

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    @ConditionalOnProperty(value = ["security.oauth2.enabled"], havingValue = "false", matchIfMissing = true)
    fun disabledSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.build()
    }

    @Bean
    @ConditionalOnProperty(value = ["security.oauth2.enabled"], havingValue = "true", matchIfMissing = false)
    fun securityFilterChain(
        http: HttpSecurity,
        jwtDecoder: JwtDecoder,
        @Value("\${security.oauth2.admin}") adminQuery: String,
    ): SecurityFilterChain {
        val adminClaimQuery = JwtClaimQuery.parse(adminQuery)
        val jwtAuthProvider = JwtAuthenticationProvider(jwtDecoder)

        http {
            authorizeHttpRequests {
                authorize(HttpMethod.POST, "/api/**", hasAuthority(ADMIN_AUTHORITY))
                authorize(HttpMethod.PUT, "/api/**", hasAuthority(ADMIN_AUTHORITY))
                authorize(HttpMethod.PATCH, "/api/**", hasAuthority(ADMIN_AUTHORITY))
                authorize(HttpMethod.DELETE, "/api/**", hasAuthority(ADMIN_AUTHORITY))
                authorize("/api/**", authenticated)
                authorize(anyRequest, permitAll)
            }
            oauth2ResourceServer {
                jwt {
                    authenticationManager = AuthenticationManager {
                        val auth = jwtAuthProvider.authenticate(it) as JwtAuthenticationToken
                        val authorities = auth.authorities.toMutableList()

                        if(adminClaimQuery.query(auth.token)) {
                            authorities.add(SimpleGrantedAuthority(ADMIN_AUTHORITY))
                        }

                        JwtAuthenticationToken(
                            auth.token,
                            authorities,
                            auth.name
                        )
                    }
                }
            }
        }

        return http.build()
    }

    @Bean
    @ConditionalOnProperty(value = ["security.oauth2.enabled"], havingValue = "true", matchIfMissing = false)
    fun createJwtDecoder(
        @Value("\${security.oauth2.issuer-url}") issuer: String,
    ): JwtDecoder {
        return JwtDecoders.fromIssuerLocation(issuer)
    }
}