package org.ivcode.homeportal.controllers

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.ivcode.homeportal.services.HealthService
import org.ivcode.homeportal.services.models.HealthInfo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health-check")
public class HealthController(
    private val healthService: HealthService
) {

    @GetMapping
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Healthy"),
        ApiResponse(responseCode = "500", description = "Unhealthy")
    ])
    public fun healthCheck(): ResponseEntity<HealthInfo> {
        val info = healthService.healthCheck()
        return if(info.success) {
            ResponseEntity.ok().body(info)
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(info)
        }
    }
}