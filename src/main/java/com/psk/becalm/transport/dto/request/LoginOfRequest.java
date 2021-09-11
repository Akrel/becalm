package com.psk.becalm.transport.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginOfRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
