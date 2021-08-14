package com.psk.becalm.api;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApi {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AppUser> userApiResponseEntity(@RequestBody AppUser user) {
        System.out.println("");
        return ResponseEntity.ok(userService.saveUser(user));
    }

}
