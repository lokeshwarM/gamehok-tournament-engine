package com.gamehok.tournament.config;

import com.gamehok.tournament.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * Custom health check endpoint providing application status and version info.
 * <p>
 * Accessible at GET /api/v1/health (no authentication required).
 * Complements the Spring Actuator /actuator/health endpoint.
 * </p>
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health", description = "Application health and version information")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Application health check")
    public ResponseEntity<ApiResponse<HealthResponse>> health() {
        HealthResponse response = HealthResponse.builder()
                .status("UP")
                .service("Gamehok Tournament Engine")
                .version("1.0.0")
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(ApiResponse.success("Service is healthy", response));
    }

    @Getter
    @Builder
    public static class HealthResponse {
        private final String status;
        private final String service;
        private final String version;
        private final Instant timestamp;
    }
}
