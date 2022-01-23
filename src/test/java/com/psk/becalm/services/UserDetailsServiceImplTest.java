package com.psk.becalm.services;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import com.psk.becalm.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDetailsServiceImplTest {

    private UserRepository userRepository;

    private UserDetailsServiceImpl userDetailsService;


    @BeforeEach
    private void init() {
        userRepository = Mockito.mock(UserRepository.class);

        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsernameTest() {
        AppUser appUser = createAppUser();

        UserDetailsImpl userDetails = UserDetailsImpl.build(appUser);

        Mockito.when(userRepository.findByUsername(appUser.getUsername())).thenReturn(Optional.of(appUser));

        userDetailsService.loadUserByUsername(appUser.getUsername());

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.eq(appUser.getUsername()));
    }

    @Test
    void testGetters() {
        AppUser appUser = createAppUser();
        UserDetailsImpl userDetails = getUserDetails(appUser);


        List<? extends GrantedAuthority> grantedAuthorities = userDetails.getAuthorities().stream().toList();
        assertEquals(1, grantedAuthorities.size());
        assertEquals(grantedAuthorities.get(0).getAuthority(), RoleUserEnum.USER.toString());

        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonExpired());
        assertEquals(appUser.getPassword(), userDetails.getPassword());
        assertEquals(appUser.getUsername(), userDetails.getUsername());

    }


    private UserDetailsImpl getUserDetails(AppUser appUser) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        return UserDetailsImpl.build(appUser);
    }

    private AppUser createAppUser() {
        String firstName = "Jan";
        String surname = "Kowalski";
        String password = "pa$$w0rd";
        String email = "jankowalski@gmail.com";
        String username = "jan.kowalski";
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleUserEnum.USER));

        return new AppUser(2L, firstName, username, surname, password, email, roles);
    }
}