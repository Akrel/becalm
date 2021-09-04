package com.psk.becalm.api;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.service.UserService;
import com.psk.becalm.transport.converters.AppUserConverter;
import com.psk.becalm.transport.dto.model.AppUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AppUser> userApiResponseEntity(@RequestBody AppUserDto appUser) {
        System.out.println("");
        return ResponseEntity.ok(userService.saveUser(AppUserConverter.toEntity(appUser)));
    }


    @PostMapping("/login")
    public ResponseEntity<AppUser> loginApiUser() {
        System.out.println("");
        return ResponseEntity.ok(userService.saveUser(null));
    }

}
