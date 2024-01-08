package org.ivcode.homeportal.services

import org.apache.http.client.utils.URIBuilder
import org.ivcode.homeportal.api.OAuth2TokenApi
import org.ivcode.homeportal.api.models.OAuth2TokenResponse
import org.ivcode.homeportal.exceptions.BadRequestException
import org.ivcode.homeportal.exceptions.ForbiddenException
import org.ivcode.homeportal.exceptions.InternalServerErrorException
import org.ivcode.homeportal.exceptions.UnauthorizedException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.net.URI

@Service
@ConditionalOnProperty(value = ["security.oauth2.enabled"], havingValue = "true", matchIfMissing = false)
public class OAuth2ClientService(
    private val oAuth2TokenApi: OAuth2TokenApi,
    @Qualifier("oauth2.redirect-path") private val redirectPath: String,
    @Value("\${security.oauth2.client.authorization-url}") private val authorizationUrl: String,
    @Value("\${security.oauth2.client.client-id}") private val clientId: String,
    @Value("\${security.oauth2.client.client-secret}") private val clientSecret: String,
) {

    public fun authUrl (requestedHost: URI):URI {
        val uri = URIBuilder(authorizationUrl)
            .addParameter("response_type", "code")
            .addParameter("client_id", clientId)
            .addParameter("redirect_uri", redirectUrl(requestedHost).toString())
            .build()

        return uri
    }

    public fun redirectUrl (requestedHost: URI):URI = URIBuilder(requestedHost.toString())
        .setPath(redirectPath)
        .build()


    public fun token (code: String, redirectUri: String): OAuth2TokenResponse {
        val tokenResponse = oAuth2TokenApi.token (
            code = code,
            redirectUri = redirectUri,
            clientId = clientId,
            clientSecret = clientSecret
        )
        val response = tokenResponse.execute()

        if(response.code() == 401) {
            throw UnauthorizedException()
        }
        if(response.code() == 403) {
            throw ForbiddenException()
        }
        if(response.code() in 400..499) {
            throw BadRequestException()
        }
        if(response.code() !in 200..299) {
            throw InternalServerErrorException()
        }

        return response.body()!!
    }

    public fun refresh (refreshToken: String): OAuth2TokenResponse {
        val tokenResponse = oAuth2TokenApi.refresh (
            refreshToken = refreshToken,
            clientId = clientId,
            clientSecret = clientSecret
        )
        val response = tokenResponse.execute()

        if(response.code() == 401) {
            throw UnauthorizedException()
        }
        if(response.code() == 403) {
            throw ForbiddenException()
        }
        if(response.code() in 400..499) {
            throw BadRequestException()
        }
        if(response.code() !in 200..299) {
            throw InternalServerErrorException()
        }

        return response.body()!!
    }
}