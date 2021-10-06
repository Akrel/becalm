package com.psk.becalm.model.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idToken;

    @OneToOne()
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private AppUser appUser;

    @Column(nullable = false, unique = true)
    private String jwtToken;

    @Column(nullable = false)
    private Instant expiryDate;
}
