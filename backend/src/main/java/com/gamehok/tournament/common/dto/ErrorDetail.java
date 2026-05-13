package com.gamehok.tournament.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * Structured error detail payload used inside {@link ApiResponse} for error responses.
 * <p>
 * Provides granular error information for client-side handling:
 * - {@code code}: Machine-readable error code (e.g., "TOURNAMENT_NOT_FOUND")
 * - {@code message}: Human-readable explanation
 * - {@code fieldErrors}: Validation errors per field (form validation support)
 * - {@code path}: The request URI that triggered the error
 * - {@code traceId}: Correlation ID for log tracing
 * </p>
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetail {

    private final String code;
    private final String message;
    private final String path;
    private final String traceId;
    private final List<FieldError> fieldErrors;

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final Object rejectedValue;
        private final String message;
    }
}
