package com.gamehok.tournament.user.repository;

import com.gamehok.tournament.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for {@link User} entity queries.
 * <p>
 * Follows the principle of keeping query logic in repositories, not services.
 * Complex queries use JPQL to avoid N+1 issues.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByGameId(String gameId);

    @Query("SELECT u FROM User u WHERE u.active = true ORDER BY u.eloRating DESC")
    Page<User> findAllActiveOrderByEloRating(Pageable pageable);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(u.displayName) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<User> searchByUsernameOrDisplayName(@Param("query") String query, Pageable pageable);
}
