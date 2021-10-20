package com.psk.becalm.transport.dto.model.auth.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtRefreshTokenResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public JwtRefreshTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
