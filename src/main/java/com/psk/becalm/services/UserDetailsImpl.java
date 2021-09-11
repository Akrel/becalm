package com.psk.becalm.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.Role;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

    private Long userId;

    private String username;

    private String name;

    private String surname;

    private String email;

    private Set<Role> roleSet;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(AppUser appUser) {
        List<GrantedAuthority> authorities = appUser.getUserRole().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().toString()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(appUser.getUserId(),
                appUser.getUsername(),
                appUser.getFirstName(),
                appUser.getSurname(),
                appUser.getEmail(),
                appUser.getUserRole(),
                appUser.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
