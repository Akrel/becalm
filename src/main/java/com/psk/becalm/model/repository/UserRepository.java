package com.psk.becalm.model.repository;

import com.psk.becalm.model.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUsername(String username);

    @Override
    Optional<AppUser> findById(Long uuid);

    Optional<AppUser> findByUsername(String username);
}
