package com.psk.becalm.model.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

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
