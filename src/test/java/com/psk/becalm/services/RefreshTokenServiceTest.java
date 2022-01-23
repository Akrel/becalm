package com.psk.becalm.services;

import com.psk.becalm.exceptions.UserAuthenticationException;
import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.RefreshToken;
import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import com.psk.becalm.model.repository.RefreshTokenRepository;
import com.psk.becalm.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.Period;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RefreshTokenServiceTest {


    @Value("${becalm.app.jwtExpirationMs}")
    private Long refreshTokenDuration;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;
    private RefreshTokenService tokenService;

    @BeforeEach
    private void init() {
        refreshTokenRepository = Mockito.mock(RefreshTokenRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        tokenService = new RefreshTokenService(refreshTokenRepository, userRepository);
        tokenService.setRefreshTokenDuration(900000L);
    }

    @Test
    void findByToken() {
        String jwtToken = "22ed816c-a370-4bd2-a1e9-57e5598fe6f8";
        AppUser appUser = createAppUser();
        RefreshToken builtRefreshToken = RefreshToken.builder().appUser(appUser)
                .jwtToken(jwtToken)
                .expiryDate(Instant.now().plusMillis(98000000))
                .build();

        Mockito.when(refreshTokenRepository.findByJwtToken(jwtToken)).thenReturn(Optional.of(builtRefreshToken));

        Optional<RefreshToken> byToken = tokenService.findByToken(jwtToken);
    }

    @Test
    void createRefreshToken() {
        AppUser appUser = createAppUser();
        RefreshToken builtRefreshToken = RefreshToken.builder().appUser(appUser)
                .jwtToken(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(98000000))
                .build();
        Mockito.when(userRepository.findById(appUser.getUserId())).thenReturn(Optional.of(appUser));

        RefreshToken refreshToken = tokenService.createRefreshToken(appUser.getUserId());


        Mockito.verify(refreshTokenRepository, Mockito.times(1)).save(Mockito.any(RefreshToken.class));
    }

    @Test
    void verifyExpiration() {
        AppUser appUser = createAppUser();
        RefreshToken builtRefreshToken = RefreshToken.builder().appUser(appUser)
                .jwtToken(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(98000000))
                .build();

        RefreshToken refreshToken = tokenService.verifyExpiration(builtRefreshToken);


        assertEquals(builtRefreshToken, refreshToken);
    }

    @Test
    void deleteByUserId() {
        String userId = UUID.randomUUID().toString();

        Mockito.when(refreshTokenRepository.deleteByAppUserUserId(UUID.fromString(userId))).thenReturn(true);

        boolean result = tokenService.deleteByUserId(userId);

        Mockito.verify(refreshTokenRepository, Mockito.times(1)).deleteByAppUserUserId(Mockito.eq(UUID.fromString(userId)));
    }

    @Test
    void verifyExpirationError() {
        String jwtToken = "22ed816c-a370-4bd2-a1e9-57e5598fe6f8";
        AppUser appUser = createAppUser();

        RefreshToken builtRefreshToken = RefreshToken.builder().appUser(appUser)
                .jwtToken(jwtToken)
                .expiryDate(Instant.now().minus(Period.ofDays(3)))
                .build();
        Mockito.doNothing().when(refreshTokenRepository).delete(builtRefreshToken);

        assertThrows(UserAuthenticationException.class, () -> {
            tokenService.verifyExpiration(builtRefreshToken);
        });

    }


    private AppUser createAppUser() {
        String firstName = "Jan";
        String surname = "Kowalski";
        String password = "pa$$w0rd";
        String email = "jankowalski@gmail.com";
        String username = "jan.kowalski";
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleUserEnum.USER));

        return new AppUser(2L, firstName, username, surname, password, email, roles);
    }
}