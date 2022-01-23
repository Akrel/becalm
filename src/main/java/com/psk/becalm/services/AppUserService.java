package com.psk.becalm.services;

import com.psk.becalm.exceptions.UserException;
import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
public class AppUserService {

    private UserRepository userRepository;

    @Autowired
    public AppUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser getAppUserById(String userId) throws UserException {
        return getAppUserById(Long.valueOf(userId));
    }

    public AppUser getAppUserById(Long userId) throws UserException {
        Optional<AppUser> userOptional = userRepository.findById(userId);
        return userOptional.orElseThrow(
                () -> new UserException(String.format("Cannot find user with id %s", userId)));
    }
}
