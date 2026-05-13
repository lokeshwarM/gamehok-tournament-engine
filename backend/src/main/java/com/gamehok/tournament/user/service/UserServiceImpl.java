package com.gamehok.tournament.user.service;

import com.gamehok.tournament.common.dto.PageResponse;
import com.gamehok.tournament.common.exception.DuplicateResourceException;
import com.gamehok.tournament.common.exception.ResourceNotFoundException;
import com.gamehok.tournament.enums.UserRole;
import com.gamehok.tournament.security.JwtService;
import com.gamehok.tournament.security.SecurityPrincipal;
import com.gamehok.tournament.user.dto.AuthResponse;
import com.gamehok.tournament.user.dto.LoginRequest;
import com.gamehok.tournament.user.dto.RegisterUserRequest;
import com.gamehok.tournament.user.dto.UserResponseDto;
import com.gamehok.tournament.user.entity.User;
import com.gamehok.tournament.user.mapper.UserMapper;
import com.gamehok.tournament.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Default implementation of {@link UserService}.
 * All dependencies injected via constructor (no field injection).
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            UserDetailsServiceImpl userDetailsService
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setDisplayName(request.getDisplayName() != null ? request.getDisplayName() : request.getUsername());
        user.setGameId(request.getGameId());
        user.setCountryCode(request.getCountryCode());
        user.setRole(UserRole.PLAYER);
        user.setEloRating(1000);

        User savedUser = userRepository.save(user);
        log.info("Registered new user: {} ({})", savedUser.getUsername(), savedUser.getUuid());

        SecurityPrincipal principal = userDetailsService.buildPrincipal(savedUser);
        return buildAuthResponse(principal, savedUser);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getEmail()));

        SecurityPrincipal principal = userDetailsService.buildPrincipal(user);
        log.info("User logged in: {} ({})", user.getEmail(), user.getUuid());
        return buildAuthResponse(principal, user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByUuid(UUID uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User", uuid));
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponseDto> searchUsers(String query, int page, int size) {
        Page<User> users = userRepository.searchByUsernameOrDisplayName(query, PageRequest.of(page, size));
        return toPageResponse(users);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponseDto> getTopPlayersByElo(int page, int size) {
        Page<User> users = userRepository.findAllActiveOrderByEloRating(PageRequest.of(page, size));
        return toPageResponse(users);
    }

    @Override
    public void updateEloRating(UUID userUuid, int newRating) {
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ResourceNotFoundException("User", userUuid));
        user.setEloRating(newRating);
        log.info("Updated ELO for user {}: {}", userUuid, newRating);
    }

    @Override
    public void deactivateUser(UUID uuid) {
        User user = userRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("User", uuid));
        user.setActive(false);
        log.info("Deactivated user: {}", uuid);
    }

    private AuthResponse buildAuthResponse(SecurityPrincipal principal, User user) {
        return AuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(principal))
                .refreshToken(jwtService.generateRefreshToken(principal))
                .tokenType("Bearer")
                .expiresIn(3600)
                .user(userMapper.toResponseDto(user))
                .build();
    }

    private PageResponse<UserResponseDto> toPageResponse(Page<User> page) {
        return PageResponse.<UserResponseDto>builder()
                .content(page.getContent().stream().map(userMapper::toResponseDto).toList())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
