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
    List<CalendarTask> findAllByAppUserAndStartDate_Month(AppUser appUser, int startDate_month);


    Optional<CalendarTask> findByIdAndAppUser(UUID uuid,AppUser appUser);
}
