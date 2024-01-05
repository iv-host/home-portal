package org.ivcode.homeportal.controllers

import io.swagger.v3.oas.annotations.Hidden
import jakarta.servlet.http.HttpServletRequest
import org.apache.http.client.utils.URIBuilder
import org.ivcode.homeportal.services.OAuth2ClientService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import java.net.URI


const val PATH_OAUTH2 = "/oauth2"
const val PATH_OAUTH2_LOGIN = "/login"
const val PATH_OAUTH2_REDIRECT = "/redirect"
const val PATH_OAUTH2_REFRESH = "/refresh"

/**
 * Facilitates a user logging in through an OAuth2 authorization server.
 * Only the Authorization Code flow is supported
 */
@Hidden
@RestController
@RequestMapping(PATH_OAUTH2)
@ConditionalOnProperty(value = ["security.oauth2.enabled"], havingValue = "true", matchIfMissing = false)
class OAuth2ClientController(
    private val oAuthClientService: OAuth2ClientService
) {

    /**
     * When a login is required, the UI will redirect the user here. This will construct authorization url and redirect
     * the user there to log in
     */
    @GetMapping(PATH_OAUTH2_LOGIN)
    fun login(
        request: HttpServletRequest
    ): ResponseEntity<Void> = ResponseEntity
        .status(HttpStatus.MOVED_PERMANENTLY)
        .header(HttpHeaders.LOCATION, oAuthClientService.authUrl(getRequestHost(request)).toString())
        .build()

    /**
     * After the user logs is authorized, they will be redirected to here. New tokens are requested and
     * set as cookies and the user is redirected back into the application.
     */
    @GetMapping(PATH_OAUTH2_REDIRECT)
    fun authorizationCodeRedirect(
        @RequestParam("code") code: String,
        request: HttpServletRequest
    ) : ResponseEntity<Void> {
        val hostUri =  getRequestHost(request)
        val redirectUri = oAuthClientService.redirectUrl(hostUri).toString()
        val tokenResponse = oAuthClientService.token(code, redirectUri)

        val accessTokenCookie: HttpCookie = ResponseCookie
            .from("access-token", tokenResponse.accessToken)
            .path("/")
            .build()

        val refreshTokenCookie: HttpCookie = ResponseCookie
            .from("refresh-token", tokenResponse.refreshToken)
            .path("/")
            .build()

        return ResponseEntity
            .status(HttpStatus.MOVED_PERMANENTLY)
            .header(HttpHeaders.LOCATION, hostUri.toString())
            .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
            .build()
    }

    /**
     * Requested by the UI to refresh tokens
     */
    @GetMapping(PATH_OAUTH2_REFRESH)
    fun refresh (
        @RequestParam("refresh_token") refreshToken: String,
    ) = oAuthClientService.refresh(refreshToken)

    private fun getRequestHost(
        request: HttpServletRequest
    ): URI = URIBuilder(request.requestURL.toString()).apply {
        path = ""
    }.build()
}