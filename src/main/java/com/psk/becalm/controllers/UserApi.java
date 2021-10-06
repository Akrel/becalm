package com.psk.becalm.controllers;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.RefreshToken;
import com.psk.becalm.model.repository.UserRepository;
import com.psk.becalm.security.jwt.JwtUtils;
import com.psk.becalm.services.RefreshTokenService;
import com.psk.becalm.services.UserDetailsImpl;
import com.psk.becalm.transport.converters.AppUserConverter;
import com.psk.becalm.transport.dto.model.AppUserDto;
import com.psk.becalm.transport.dto.request.LoginOfRequest;
import com.psk.becalm.transport.dto.response.JwtOfResponse;
import com.psk.becalm.transport.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/auth", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE})
@RequiredArgsConstructor
public class UserApi {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtils jwtUil;

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
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginOfRequest loginRequest) {
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(String uuid) {
        refreshTokenService.deleteByUserId(uuid);
        return ResponseEntity.ok(MessageResponse.of("Log out"));
    }
}
