package com.gamehok.tournament.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * JPA Auditing configuration providing the current auditor (username) from the security context.
 * <p>
 * This is referenced via {@code @EnableJpaAuditing(auditorAwareRef = "auditorAwareProvider")}
 * in {@link com.gamehok.tournament.TournamentEngineApplication}.
 * </p>
 */
@Configuration
public class AuditingConfig {

    @Bean
    public AuditorAware<String> auditorAwareProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()
                    || "anonymousUser".equals(authentication.getPrincipal())) {
                return Optional.of("SYSTEM");
            }
            return Optional.of(authentication.getName());
        };
    }
}
