package com.psk.becalm.transport.dto.model.auth.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;

}
