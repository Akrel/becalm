package com.psk.becalm.transport.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AppUserDto {
    private String name;
    private String surname;
    private String password;
    private String email;
    private String role;
}
