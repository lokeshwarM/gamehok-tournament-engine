package com.gamehok.tournament.user.service;

import com.gamehok.tournament.common.exception.ResourceNotFoundException;
import com.gamehok.tournament.enums.UserRole;
import com.gamehok.tournament.security.SecurityPrincipal;
import com.gamehok.tournament.user.entity.User;
import com.gamehok.tournament.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Security {@link UserDetailsService} implementation.
 * Loads user by email for authentication and builds a {@link SecurityPrincipal}.
 */
@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return buildPrincipal(user);
    }

    public SecurityPrincipal buildPrincipal(User user) {
        return SecurityPrincipal.builder()
                .userUuid(user.getUuid())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPasswordHash())
                .enabled(user.isActive())
                .accountNonLocked(!user.isLocked())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                .build();
    }
}
