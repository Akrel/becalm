package com.psk.becalm.controllers;

import com.psk.becalm.model.entities.ToDoTask;
import com.psk.becalm.services.ToDoService;
import com.psk.becalm.services.UserDetailsImpl;
import com.psk.becalm.transport.converters.TodoTaskConverter;
import com.psk.becalm.transport.dto.response.MessageResponse;
import com.psk.becalm.transport.dto.todo.ToDoTaskDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured("USER")
@Slf4j
@RequestMapping(value = "/todoTask")
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
    public ResponseEntity<?> removeTask(@RequestParam String taskUUid) {
        UserDetailsImpl principal = getPrincipal();

        try {
            toDoService.removeToDoTask(principal.getUserId(), taskUUid);
            return ResponseEntity.ok().body(MessageResponse.of("Task has been removed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/addNew")
    public ResponseEntity<?> addNewTask(@RequestParam ToDoTaskDto taskDto) {
        UserDetailsImpl principal = getPrincipal();
        try {
            ToDoTask toDoTask = toDoService.addNewTodoTask(principal.getUserId(), taskDto);
            return ResponseEntity.ok().body(TodoTaskConverter.toDto(toDoTask));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private UserDetailsImpl getPrincipal() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
