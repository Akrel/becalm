package com.psk.becalm.services;

import com.psk.becalm.exceptions.UserAuthenticationException;
import com.psk.becalm.model.entities.RefreshToken;
import com.psk.becalm.model.repository.RefreshTokenRepository;
import com.psk.becalm.model.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Setter
    @Value("${becalm.app.jwtExpirationMs}")
    private Long refreshTokenDuration;

    private RefreshTokenRepository refreshTokenRepository;

    private UserRepository userRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }


    public Optional<RefreshToken> findByToken(String jwtToken) {
        return refreshTokenRepository.findByJwtToken(jwtToken);
    }

    public RefreshToken createRefreshToken(Long userUuid) {
        RefreshToken builtRefreshToken = RefreshToken.builder().appUser(userRepository.findById(userUuid).orElseThrow())
                .jwtToken(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDuration))
                .build();

        return refreshTokenRepository.save(builtRefreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new UserAuthenticationException("Refresh token was expired. Please make a new signin request");
        }
        return refreshToken;
    }

    @Transactional
    public boolean deleteByUserId(String userId) {
        return refreshTokenRepository.deleteByAppUserUserId(UUID.fromString(userId));
    }

}
