package com.psk.becalm.model.repository;

import com.psk.becalm.model.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Override
    Optional<AppUser> findById(Long uuid);

    Optional<AppUser> findByUsername(String username);
}
