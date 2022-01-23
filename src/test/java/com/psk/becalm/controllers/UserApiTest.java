package com.psk.becalm.controllers;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.RefreshToken;
import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import com.psk.becalm.model.repository.UserRepository;
import com.psk.becalm.security.jwt.JwtUtils;
import com.psk.becalm.services.RefreshTokenService;
import com.psk.becalm.services.UserDetailsImpl;
import com.psk.becalm.transport.converters.AppUserConverter;
import com.psk.becalm.transport.dto.model.AppUserDto;
import com.psk.becalm.transport.dto.model.auth.request.TokenRefreshRequest;
import com.psk.becalm.transport.dto.model.auth.response.JwtRefreshTokenResponse;
import com.psk.becalm.transport.dto.response.MessageResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        ResponseEntity<?> responseEntity = userApi.registerUser(appUserDto);

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.eq(appUser));
    }

    @Test
    void registerError() {
        String firstName = "Jan";
        String surname = "Kowalski";
        String password = "pa$$w0rd";
        String email = "jankowalski@gmail.com";
        String username = "jan.kowalski";

        AppUserDto appUserDto = new AppUserDto(firstName, surname, password, email, username);

        Mockito.when(userRepository.existsByEmail(email)).thenReturn(true);
        Mockito.when(userRepository.existsByUsername(username)).thenReturn(true);

        ResponseEntity<?> responseEntity = userApi.registerUser(appUserDto);


        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MessageResponse.of("Username is already taken"), responseEntity.getBody());
    }


    @Test
    void refreshTokenTest() {
        String jwtToken = "22ed816c-a370-4bd2-a1e9-57e5598fe6f8";

        String firstName = "Jan";
        String surname = "Kowalski";
        String password = "pa$$w0rd";
        String email = "jankowalski@gmail.com";
        String username = "jan.kowalski";
        Role role = new Role(RoleUserEnum.USER);

        AppUser appUser = new AppUser(2L, firstName, username, surname, password, email, Set.of(role));

        String compact = Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 3000))
                .signWith(SignatureAlgorithm.HS512, "secret")
                .compact();

        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken(jwtToken);
        RefreshToken refreshToken = new RefreshToken(12L, appUser, jwtToken, Instant.now());

        Mockito.when(refreshTokenService.findByToken(jwtToken)).thenReturn(Optional.of(refreshToken));
        Mockito.when(refreshTokenService.verifyExpiration(refreshToken)).thenReturn(refreshToken);

        Mockito.when(jwtUil.generateTokenFromUsername(username)).thenReturn(compact);


        ResponseEntity<?> responseEntity = userApi.refreshtoken(refreshRequest);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getStatusCode());
        assertEquals(new JwtRefreshTokenResponse(compact, jwtToken), responseEntity.getBody());
    }

    @Test
    void logoutTest() {
        String uuid = UUID.randomUUID().toString();

        Mockito.when(refreshTokenService.deleteByUserId(uuid)).thenReturn(true);

        ResponseEntity<?> responseEntity = userApi.logout(uuid);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MessageResponse.of("Log out"), responseEntity.getBody());
    }

    @Test
    void loginTest() {
/*        RequestLogin requestLogin = new RequestLogin();
        requestLogin.setPassword("pa$$w0rd");
        requestLogin.setUsername("jan.kowalski");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(requestLogin.getUsername(), requestLogin.getPassword(),
                AuthorityUtils
                        .commaSeparatedStringToAuthorityList(RoleUserEnum.USER.toString())
        );
        AppUser appUser = createAppUser();
        String jwtToken = "22ed816c-a370-4bd2-a1e9-57e5598fe6f8";

        OngoingStubbing<Authentication> authenticationOngoingStubbing = Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestLogin.getUsername(), requestLogin.getPassword())))
                .thenReturn(authenticationToken);

        UserDetailsImpl userDetails = getUserDetails(appUser);
        Mockito.doNothing().when(SecurityContextHolder.getContext()).setAuthentication(authenticationToken);



        Mockito.when(jwtUil.generateJwtToken(userDetails)).thenReturn(jwtToken);

        UserDetailsImpl userDetails1 = new UserDetailsImpl(2L, appUser.getUsername(), appUser.getFirstName(), appUser.getSurname(), appUser.getEmail(), appUser.getUserRole(), appUser.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(RoleUserEnum.USER.toString()));


        ResponseEntity<?> responseEntity = userApi.loginUser(requestLogin);*/

    }

    @Test
    void convertAppUserToDTO() {
        String firstName = "Jan";
        String surname = "Kowalski";
        String password = "pa$$w0rd";
        String email = "jankowalski@gmail.com";
        String username = "jan.kowalski";
        Role role = new Role(RoleUserEnum.USER);

        AppUser appUser = new AppUser(2L, firstName, username, surname, password, email, Set.of(role));

        AppUserDto appUserDto = AppUserConverter.toDto(appUser);

        assertEquals(firstName, appUserDto.getFirstName());
        assertEquals(surname, appUserDto.getSurname());
        assertEquals(password, appUserDto.getPassword());
        assertEquals(email, appUserDto.getEmail());
        assertEquals(username, appUserDto.getUsername());

    }

    private UserDetailsImpl getUserDetails(AppUser appUser) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        return UserDetailsImpl.build(appUser);
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