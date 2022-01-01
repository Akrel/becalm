package com.psk.becalm.model.repository;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.ToDoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ToDoTaskRepository extends JpaRepository<ToDoTask, UUID> {

    List<ToDoTask> findToDoTaskByAppUser(AppUser appUser);

    Optional<ToDoTask> findToDoTaskByAppUserAndTaskId(AppUser appUser, UUID uuid);

    @Override
    Optional<ToDoTask> findById(UUID uuid);
}
