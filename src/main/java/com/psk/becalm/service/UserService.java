package com.psk.becalm.service;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.Role;
import org.apache.catalina.User;

public interface UserService {
    AppUser saveUser(AppUser appUser);

    void addRoleToUser(String email, String roleName) throws Exception;

    AppUser getUser(String email);

    Role saveRole(Role role);
}
