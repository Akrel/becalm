package com.psk.becalm.transport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class JwtOfResponse {

    private String jwtToken;
    private String type = "Bearer";
    private String jwtRefreshToken;
    private Long uuid;
    private String userName;
    private String email;
    private List<String> userRole;

    public JwtOfResponse(String jwt, String jwtToken, Long uuid, String email, String userName, List<String> roles) {
        this.jwtToken = jwt;
        this.jwtRefreshToken = jwtToken;
        this.uuid = uuid;
        this.email = email;
        this.userName = userName;
        this.userRole = roles;
    }
}
