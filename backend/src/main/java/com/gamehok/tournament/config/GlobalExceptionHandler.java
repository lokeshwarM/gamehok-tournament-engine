package com.gamehok.tournament.config;

import com.gamehok.tournament.common.dto.ApiResponse;
import com.gamehok.tournament.common.dto.ErrorDetail;
import com.gamehok.tournament.common.exception.TournamentEngineException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Centralized global exception handler for all REST controllers.
 * <p>
 * Intercepts all exceptions thrown from controllers, services, and repositories,
 * wrapping them in {@link ApiResponse} with structured {@link ErrorDetail}.
 * <p>
 * All errors include a unique traceId for log correlation.
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TournamentEngineException.class)
    public ResponseEntity<ApiResponse<Void>> handleTournamentEngineException(
            TournamentEngineException ex,
            HttpServletRequest request
    ) {
        String traceId = generateTraceId();
        log.error("[{}] TournamentEngineException: {} | path={}", traceId, ex.getMessage(), request.getRequestURI());

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code(ex.getErrorCode())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.error(ex.getMessage(), errorDetail));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String traceId = generateTraceId();
        log.warn("[{}] Validation failed on request to {}", traceId, request.getRequestURI());

        List<ErrorDetail.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> ErrorDetail.FieldError.builder()
                        .field(fe.getField())
                        .rejectedValue(fe.getRejectedValue())
                        .message(fe.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("VALIDATION_FAILED")
                .message("Request validation failed")
                .path(request.getRequestURI())
                .traceId(traceId)
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed", errorDetail));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        String traceId = generateTraceId();
        log.warn("[{}] Constraint violations on request to {}", traceId, request.getRequestURI());

        List<ErrorDetail.FieldError> fieldErrors = ex.getConstraintViolations().stream()
                .map(cv -> ErrorDetail.FieldError.builder()
                        .field(extractFieldName(cv))
                        .rejectedValue(cv.getInvalidValue())
                        .message(cv.getMessage())
                        .build())
                .collect(Collectors.toList());

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("CONSTRAINT_VIOLATION")
                .message("Constraint violation")
                .path(request.getRequestURI())
                .traceId(traceId)
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Constraint violation", errorDetail));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        String traceId = generateTraceId();
        log.warn("[{}] Bad credentials for request to {}", traceId, request.getRequestURI());

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("INVALID_CREDENTIALS")
                .message("Invalid username or password")
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Authentication failed", errorDetail));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        String traceId = generateTraceId();
        String message = String.format("Parameter '%s' has invalid value '%s'", ex.getName(), ex.getValue());
        log.warn("[{}] Type mismatch: {}", traceId, message);

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("TYPE_MISMATCH")
                .message(message)
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(message, errorDetail));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        String traceId = generateTraceId();
        log.error("[{}] Unhandled exception on request to {}: {}", traceId, request.getRequestURI(), ex.getMessage(), ex);

        ErrorDetail errorDetail = ErrorDetail.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error", errorDetail));
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    private String extractFieldName(ConstraintViolation<?> cv) {
        String propertyPath = cv.getPropertyPath().toString();
        int lastDot = propertyPath.lastIndexOf('.');
        return lastDot >= 0 ? propertyPath.substring(lastDot + 1) : propertyPath;
    }
}
