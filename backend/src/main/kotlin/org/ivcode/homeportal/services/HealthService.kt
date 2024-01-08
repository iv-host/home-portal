package org.ivcode.homeportal.services

import org.ivcode.homeportal.repositories.HealthDao
import org.ivcode.homeportal.services.models.HealthInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
public class HealthService(
    private val healthDao: HealthDao,
    @Value("\${security.oauth2.enabled:false}") private val isOAuthEnabled: Boolean,
    @Value("\${security.oauth2.issuer-url:#{null}}") private val oauthIssuerUrl: String?,
) {

    public fun healthCheck(): HealthInfo {

        // Check for database connectivity
        val databaseTest = testDatabase()

        // Check for the issuer's availability
        val issuerTest = testIssuer()

        return HealthInfo(
            success = databaseTest.success && issuerTest.success,
            database = databaseTest.message,
            issuer = issuerTest.message,
        )
    }

    private fun testDatabase(): HealthTestResult {
        return try {
            if(healthDao.healthCheck() == 1) {
                HealthTestResult(
                    success = true,
                    message = "available"
                )
            } else {
                HealthTestResult(
                    success = false,
                    message = "unexpected result"
                )
            }
        } catch (e: Exception) {
            HealthTestResult(
                success = false,
                message = "unavailable"
            )
        }
    }

    private fun testIssuer(): HealthTestResult {
        return try {
            if (isOAuthEnabled) {
                if (testUrl(oauthIssuerUrl!!) == 200) {
                    // OAuth2 is enabled and the issuer-url is available
                    HealthTestResult(
                        success = true,
                        message = "available"
                    )
                } else {
                    // OAuth2 is enabled and the issuer-url is unavailable
                    HealthTestResult(
                        success = false,
                        message = "unavailable"
                    )
                }
            } else {
                // OAuth2 is disabled
                HealthTestResult(
                    success = true,
                    message = "disabled"
                )
            }
        } catch (e: Exception) {
            // Unexpected error
            HealthTestResult(
                success = false,
                message = "unexpected error"
            )
        }
    }

    private fun testUrl(url: String): Int {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response.statusCode()
    }
}

private data class HealthTestResult (
    val success: Boolean,
    val message: String
)