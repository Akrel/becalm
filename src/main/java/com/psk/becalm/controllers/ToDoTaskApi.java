package com.psk.becalm.controllers;

import com.psk.becalm.model.entities.ToDoTask;
import com.psk.becalm.services.ToDoService;
import com.psk.becalm.services.UserDetailsImpl;
import com.psk.becalm.transport.converters.TodoTaskConverter;
import com.psk.becalm.transport.dto.response.MessageResponse;
import com.psk.becalm.transport.dto.todo.ToDoTaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@Slf4j
@RequestMapping(value = "/todoTask", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_PLAIN_VALUE})
public class ToDoTaskApi {

    @Autowired
    private ToDoService toDoService;


    @GetMapping("/getAll")
    public ResponseEntity<List<ToDoTaskDto>> getTasks() {
        UserDetailsImpl principal = getPrincipal();
        List<ToDoTaskDto> allTodoTask = toDoService.getAllTodoTask(principal.getUserId());
        return ResponseEntity.ok(allTodoTask);
    }

    @DeleteMapping("/remove/{taskTodoUuid}")
    public ResponseEntity<?> removeTask(@PathVariable String taskTodoUuid) {
        UserDetailsImpl principal = getPrincipal();

        toDoService.removeToDoTask(principal.getUserId(), taskTodoUuid);
        return ResponseEntity.ok().body(MessageResponse.of("Task has been removed"));
    }


    @PutMapping("/addNew")
    public ResponseEntity<ToDoTaskDto> addNewTask(@RequestBody ToDoTaskDto toDoTaskDto) {
        UserDetailsImpl principal = getPrincipal();

        ToDoTask toDoTask = toDoService.addNewTodoTask(principal.getUserId(), toDoTaskDto);
        return ResponseEntity.ok().body(TodoTaskConverter.toDto(toDoTask));
    }

    @PutMapping("/toogleTask/{taskTodoUuid}")
    public ResponseEntity<ToDoTask> toogleTask(@PathVariable String taskTodoUuid) {
        UserDetailsImpl principal = getPrincipal();
        ToDoTask toDoTask = toDoService.toogleTaskTodo(principal.getUserId(), taskTodoUuid);
        return ResponseEntity.ok().body(toDoTask);
    }

    @PutMapping("/editTodo")
    public ResponseEntity<ToDoTask> editTask(@RequestBody ToDoTaskDto toDoTaskDto) {

        ToDoTask toDoTask = toDoService.editTodoTask(toDoTaskDto);
        return ResponseEntity.ok().body(toDoTask);
    }


    private UserDetailsImpl getPrincipal() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
