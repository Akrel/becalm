package com.psk.becalm.controllers;

import com.psk.becalm.exceptions.TokenRefreshException;
import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.RefreshToken;
import com.psk.becalm.model.repository.UserRepository;
import com.psk.becalm.security.jwt.JwtUtils;
import com.psk.becalm.services.RefreshTokenService;
import com.psk.becalm.services.UserDetailsImpl;
import com.psk.becalm.transport.converters.AppUserConverter;
import com.psk.becalm.transport.dto.model.AppUserDto;
import com.psk.becalm.transport.dto.model.auth.request.RequestLogin;
import com.psk.becalm.transport.dto.model.auth.request.TokenRefreshRequest;
import com.psk.becalm.transport.dto.model.auth.response.JwtOfResponse;
import com.psk.becalm.transport.dto.model.auth.response.JwtRefreshTokenResponse;
import com.psk.becalm.transport.dto.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/auth")
public class UserApi {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUil;

    @Autowired
    public UserApi(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService, JwtUtils jwtUil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.jwtUil = jwtUil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody AppUserDto appUserDto) {
        if (userRepository.existsByUsername(appUserDto.getUsername()) && userRepository.existsByEmail(appUserDto.getEmail()))
            return ResponseEntity.badRequest()
                    .body(MessageResponse.of("Username is already taken"));

        AppUser appUser = AppUserConverter.toEntity(appUserDto);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        userRepository.save(appUser);
        return ResponseEntity.ok().body(MessageResponse.of("User registered successfully!"));
    }


    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@Valid @RequestBody RequestLogin loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUil.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUserId());

        return ResponseEntity.ok(new JwtOfResponse(jwt, refreshToken.getJwtToken(), userDetails.getUserId(), userDetails.getEmail(), userDetails.getUsername(), roles));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getAppUser)
                .map(user -> {
                    String token = jwtUil.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new JwtRefreshTokenResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(String uuid) {
        refreshTokenService.deleteByUserId(uuid);
        return ResponseEntity.ok(MessageResponse.of("Log out"));
    }
}
