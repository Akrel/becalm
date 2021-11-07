package com.psk.becalm.services;

import com.psk.becalm.exceptions.UserException;
import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {
    @Autowired
    private UserRepository userRepository;

    public AppUser getAppUserById(String userId) throws UserException {
        return getAppUserById(Long.valueOf(userId));
    }

    public AppUser getAppUserById(Long userId) throws UserException {
        Optional<AppUser> userOptional = userRepository.findById(userId);
        return userOptional.orElseThrow(
                () -> new UserException(String.format("Cannot find user with id %s", userId)));
    }
}
