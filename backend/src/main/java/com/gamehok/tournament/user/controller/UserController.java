package com.gamehok.tournament.user.controller;

import com.gamehok.tournament.common.dto.ApiResponse;
import com.gamehok.tournament.common.dto.PageResponse;
import com.gamehok.tournament.user.dto.AuthResponse;
import com.gamehok.tournament.user.dto.LoginRequest;
import com.gamehok.tournament.user.dto.RegisterUserRequest;
import com.gamehok.tournament.user.dto.UserResponseDto;
import com.gamehok.tournament.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for user management and authentication endpoints.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Users", description = "User registration, authentication, and profile management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    @Operation(summary = "Register a new user account")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterUserRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/auth/login")
    @Operation(summary = "Authenticate user and obtain JWT tokens")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @GetMapping("/users/me")
    @Operation(summary = "Get current authenticated user profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUser() {
        return ResponseEntity.ok(ApiResponse.success(userService.getCurrentUser()));
    }

    @GetMapping("/users/{uuid}")
    @Operation(summary = "Get user profile by UUID")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByUuid(@PathVariable UUID uuid) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserByUuid(uuid)));
    }

    @GetMapping("/users/search")
    @Operation(summary = "Search users by username or display name")
    public ResponseEntity<ApiResponse<PageResponse<UserResponseDto>>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(userService.searchUsers(query, page, size)));
    }

    @GetMapping("/users/leaderboard")
    @Operation(summary = "Get top players by ELO rating")
    public ResponseEntity<ApiResponse<PageResponse<UserResponseDto>>> getLeaderboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(userService.getTopPlayersByElo(page, size)));
    }

    @DeleteMapping("/admin/users/{uuid}")
    @Operation(summary = "Deactivate a user account (Admin only)")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable UUID uuid) {
        userService.deactivateUser(uuid);
        return ResponseEntity.ok(ApiResponse.success("User deactivated", null));
    }
}
