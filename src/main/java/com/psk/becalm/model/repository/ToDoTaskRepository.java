package com.psk.becalm.model.repository;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.ToDoTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ToDoTaskRepository extends JpaRepository<ToDoTask, UUID> {

    List<ToDoTask> findToDoTaskByAppUser(AppUser appUser);

    Optional<ToDoTask> findToDoTaskByAppUserAndTaskId(AppUser appUser, UUID uuid);
}
