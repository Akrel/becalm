package com.psk.becalm.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/view")
@RequiredArgsConstructor
public class View {

    @PostMapping("/register")
    public ResponseEntity<?> userApiResponseEntity() {
        System.out.println();

        return ResponseEntity.ok("a");
    }
}
