package com.psk.becalm.transport.dto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Builder
public class AppUserDto {

    @NotBlank
    private String firstName;

    @NotBlank
    @Size(min = 3, max = 20)
    private String surname;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;

    @Email
    private String email;

    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

}
