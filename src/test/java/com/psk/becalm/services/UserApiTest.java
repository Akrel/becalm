package com.psk.becalm.services;

import com.psk.becalm.controllers.UserApi;
import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import com.psk.becalm.model.repository.UserRepository;
import com.psk.becalm.security.jwt.JwtUtils;
import com.psk.becalm.transport.dto.model.AppUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

class UserApiTest {

    private UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;

    private RefreshTokenService refreshTokenService;

    private JwtUtils jwtUil;
    private UserApi userApi;

    @BeforeEach
    void init() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.authenticationManager = Mockito.mock(AuthenticationManager.class);
        this.passwordEncoder = Mockito.mock(PasswordEncoder.class);
        this.refreshTokenService = Mockito.mock(RefreshTokenService.class);
        this.jwtUil = Mockito.mock(JwtUtils.class);
        this.userApi = new UserApi(userRepository, authenticationManager, passwordEncoder, refreshTokenService, jwtUil);
    }

    @Test
    void registerUser() {
        String firstName = "Jan";
        String surname = "Kowalski";
        String password = "pa$$w0rd";
        String email = "jankowalski@gmail.com";
        String username = "jan.kowalski";

        AppUserDto appUserDto = new AppUserDto(firstName, surname, password, email, username);
        userRepository.existsByUsername(username);
        Mockito.when(userRepository.existsByUsername(username)).thenReturn(false);
        userRepository.existsByEmail(email);
        Mockito.when(userRepository.existsByEmail(email)).thenReturn(false);
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleUserEnum.USER));
        AppUser appUser = new AppUser(null, firstName, username, surname, password, email, roles);
        String expectedPassword = passwordEncoder.encode(password);
        Mockito.when(passwordEncoder.encode(password)).thenReturn(expectedPassword);
        userRepository.save(appUser);
        Mockito.when(userRepository.save(appUser)).thenReturn(appUser);

        userApi.registerUser(appUserDto);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.eq(appUser));
    }

}