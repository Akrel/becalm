package com.psk.becalm.model.repository;

import com.psk.becalm.model.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByJwtToken(String jwtToken);

    @Modifying
    boolean deleteByAppUserUserId(UUID userId);
}
