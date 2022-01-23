package com.psk.becalm.services;

import com.psk.becalm.exceptions.UserException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

class AppUserServiceTest {

    private UserRepository userRepository;

    private AppUserService appUserService;

    @BeforeEach
    private void init() {
        userRepository = Mockito.mock(UserRepository.class);
        appUserService = new AppUserService(userRepository);
    }

    @Test
    void getAppUserById() throws UserException {
        AppUser appUser = createAppUser();
        Mockito.when(userRepository.findById(appUser.getUserId())).thenReturn(Optional.of(appUser));

        AppUser appUserById = appUserService.getAppUserById(appUser.getUserId().toString());

        Mockito.verify(userRepository).findById(appUser.getUserId());
    }

    @Test
    void getAppUserByIdError() {
        AppUser appUser = createAppUser();

        Mockito.when(userRepository.findById(appUser.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> {
            AppUser appUserById = appUserService.getAppUserById(appUser.getUserId().toString());
        });

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