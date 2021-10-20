package com.psk.becalm.model.repository;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.CalendarTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalendarTaskRepository extends JpaRepository<CalendarTask, UUID> {
    List<CalendarTask> findAllByAppUser(AppUser appUser);


    Optional<CalendarTask> findByTaskIdAndAppUser(UUID uuid, AppUser appUser);
}
