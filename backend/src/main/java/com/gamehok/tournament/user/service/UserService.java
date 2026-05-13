package com.gamehok.tournament.user.service;

import com.gamehok.tournament.common.dto.PageResponse;
import com.gamehok.tournament.user.dto.AuthResponse;
import com.gamehok.tournament.user.dto.LoginRequest;
import com.gamehok.tournament.user.dto.RegisterUserRequest;
import com.gamehok.tournament.user.dto.UserResponseDto;

import java.util.UUID;

/**
 * User domain service contract.
 * <p>
 * Defines operations for user lifecycle, authentication, and profile management.
 * All implementations must use constructor injection.
 * </p>
 */
public interface UserService {

    AuthResponse register(RegisterUserRequest request);

    AuthResponse login(LoginRequest request);

    UserResponseDto getUserByUuid(UUID uuid);

    UserResponseDto getCurrentUser();

    PageResponse<UserResponseDto> searchUsers(String query, int page, int size);

    PageResponse<UserResponseDto> getTopPlayersByElo(int page, int size);

    void updateEloRating(UUID userUuid, int newRating);

    void deactivateUser(UUID uuid);
}
