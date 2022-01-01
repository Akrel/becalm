package com.psk.becalm.services;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import com.psk.becalm.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    private AppUser createAppUser() {
        String firstName = "Jan";
        String surname = "Kowalski";
        String password = "pa$$w0rd";
        String email = "jankowalski@gmail.com";
        String username = "jan.kowalski";
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleUserEnum.USER));

        return new AppUser(null, firstName, username, surname, password, email, roles);
    }
}