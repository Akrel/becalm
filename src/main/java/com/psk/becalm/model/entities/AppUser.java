package com.psk.becalm.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String firstName;

    @Column(unique = true)
    private String username;

    private String surname;

    private String password;

    private String email;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Role> userRole;
}
