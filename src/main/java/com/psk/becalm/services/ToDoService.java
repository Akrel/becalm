package com.psk.becalm.services;

import com.psk.becalm.exceptions.UserException;
import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.ToDoTask;
import com.psk.becalm.model.repository.ToDoTaskRepository;
import com.psk.becalm.transport.converters.TodoTaskConverter;
import com.psk.becalm.transport.dto.todo.ToDoTaskDto;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ToDoService {
    @Autowired
    private final ToDoTaskRepository toDoTaskRepository;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    public ToDoService(AppUserService appUserService, ToDoTaskRepository toDoTaskRepository) {
        this.toDoTaskRepository = toDoTaskRepository;
        this.appUserService = appUserService;
    }

    @SneakyThrows
    public List<ToDoTaskDto> getAllTodoTask(Long appUserId) {
        AppUser appUser = appUserService.getAppUserById(appUserId);
        return toDoTaskRepository.findToDoTaskByAppUser(appUser)
                .stream().map(TodoTaskConverter::toDto)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public ToDoTask addNewTodoTask(Long appUserId, ToDoTaskDto taskDto) {
        AppUser appUser = appUserService.getAppUserById(appUserId);
        ToDoTask toDoTask = TodoTaskConverter.toEntity(taskDto);
        toDoTask.setAppUser(appUser);

        return toDoTaskRepository.save(toDoTask);
    }

    @SneakyThrows
    public ToDoTask editTodoTask(ToDoTaskDto taskDto) {
        ToDoTask taskExists = toDoTaskRepository.getById(UUID.fromString(taskDto.getUid()));

        ToDoTask toDoTask = TodoTaskConverter.toEntity(taskDto);
        toDoTask.setTaskId(taskExists.getTaskId());
        toDoTask.setAppUser(taskExists.getAppUser());

        return toDoTaskRepository.save(toDoTask);
    }

    @SneakyThrows
    public void removeToDoTask(Long appUserId, String taskUUid) {
        AppUser appUser = appUserService.getAppUserById(appUserId);
        Optional<ToDoTask> byId = toDoTaskRepository.findToDoTaskByAppUserAndTaskId(appUser, UUID.fromString(taskUUid));

        ToDoTask toDoTask = byId.orElseThrow(() -> new UserException(String.format("Cannot find todo task with id %s", taskUUid)));
        toDoTaskRepository.delete(toDoTask);
    }

    @SneakyThrows
    public ToDoTask toogleTaskTodo(Long appUserId, String taskUUid) {
        AppUser appUser = appUserService.getAppUserById(appUserId);
        Optional<ToDoTask> optionalToDoTask = toDoTaskRepository.findToDoTaskByAppUserAndTaskId(appUser, UUID.fromString(taskUUid));

        ToDoTask toDoTask = optionalToDoTask
                .orElseThrow(() -> new UserException(String.format("Cannot find todo task with id %s", taskUUid)));
        toDoTask.setDone(!toDoTask.isDone());
        return toDoTaskRepository.save(toDoTask);
    }


}
