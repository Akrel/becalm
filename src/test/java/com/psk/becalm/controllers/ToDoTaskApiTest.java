package com.psk.becalm.controllers;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import com.psk.becalm.model.entities.ToDoTask;
import com.psk.becalm.services.ToDoService;
import com.psk.becalm.services.UserDetailsImpl;
import com.psk.becalm.transport.converters.TodoTaskConverter;
import com.psk.becalm.transport.dto.response.MessageResponse;
import com.psk.becalm.transport.dto.todo.ToDoTaskDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ToDoTaskApiTest {

    private ToDoService toDoService;
    private ToDoTaskApi toDoTaskApi;

    @BeforeEach
    private void init() {

        toDoService = Mockito.mock(ToDoService.class);
        toDoTaskApi = new ToDoTaskApi(toDoService);
    }

    @Test
    void getTasks() {
        AppUser appUser = createAppUser();
        UserDetailsImpl userDetails = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);

        ToDoTaskDto taskDto = new ToDoTaskDto(true, "Opis", UUID.randomUUID().toString());
        ToDoTaskDto taskDto2 = new ToDoTaskDto(false, "Opis2", UUID.randomUUID().toString());
        ArrayList<ToDoTaskDto> toDoTaskDtos = new ArrayList<>();
        toDoTaskDtos.add(taskDto2);
        toDoTaskDtos.add(taskDto);

        Mockito.when(toDoService.getAllTodoTask(appUser.getUserId())).thenReturn(toDoTaskDtos);

        ResponseEntity<List<ToDoTaskDto>> tasks = toDoTaskApi.getTasks();

        ResponseEntity<ArrayList<ToDoTaskDto>> body = ResponseEntity.ok().body(toDoTaskDtos);


        assertEquals(HttpStatus.OK, body.getStatusCode());
    }


    @Test
    void addNewTask() {
        AppUser appUser = createAppUser();
        UserDetailsImpl userDetails = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        UUID uuid = UUID.randomUUID();
        ToDoTaskDto taskDto = new ToDoTaskDto(true, "Opis", uuid.toString());
        ToDoTask toDoTask = TodoTaskConverter.toEntity(taskDto);
        toDoTask.setTaskId(uuid);
        Mockito.when(toDoService.addNewTodoTask(appUser.getUserId(), taskDto)).thenReturn(toDoTask);
        ResponseEntity<ToDoTaskDto> toDoTaskDtoResponseEntity = toDoTaskApi.addNewTask(taskDto);


        assertEquals(HttpStatus.OK, toDoTaskDtoResponseEntity.getStatusCode());
        assertEquals(taskDto, toDoTaskDtoResponseEntity.getBody());

    }

    @Test
    void toggleTask() {
        AppUser appUser = createAppUser();
        UserDetailsImpl userDetails = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        UUID uuid = UUID.randomUUID();
        ToDoTaskDto taskDto = new ToDoTaskDto(true, "Opis", uuid.toString());
        ToDoTask toDoTask = TodoTaskConverter.toEntity(taskDto);
        toDoTask.setTaskId(uuid);
        toDoTask.setAppUser(appUser);
        toDoTask.setDone(false);
        Mockito.when(toDoService.toggleTaskTodo(appUser.getUserId(), toDoTask.getTaskId().toString())).thenReturn(toDoTask);

        ResponseEntity<ToDoTask> toDoTaskResponseEntity = toDoTaskApi.toggleTask(uuid.toString());

        assertEquals(HttpStatus.OK, toDoTaskResponseEntity.getStatusCode());
        assertNotNull(toDoTaskResponseEntity.getBody());
        assertFalse(toDoTaskResponseEntity.getBody().isDone());
    }

    @Test
    void editTask() {
        AppUser appUser = createAppUser();
        UserDetailsImpl userDetails = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        UUID uuid = UUID.randomUUID();
        ToDoTaskDto taskDto = new ToDoTaskDto(true, "Opis", uuid.toString());
        ToDoTask toDoTask = TodoTaskConverter.toEntity(taskDto);
        toDoTask.setTaskId(uuid);
        toDoTask.setAppUser(appUser);

        Mockito.when(toDoService.editTodoTask(taskDto)).thenReturn(toDoTask);

        ResponseEntity<ToDoTask> toDoTaskResponseEntity = toDoTaskApi.editTask(taskDto);


        assertEquals(HttpStatus.OK, toDoTaskResponseEntity.getStatusCode());
        assertNotNull(toDoTaskResponseEntity.getBody());

    }

    @Test
    void removeTaskTest() {
        AppUser appUser = createAppUser();
        UserDetailsImpl userDetails = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);

        Mockito.doNothing().when(toDoService).removeToDoTask(appUser.getUserId(), "12");

        ResponseEntity<?> responseEntity = toDoTaskApi.removeTask("12");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(MessageResponse.of("Task has been removed"), responseEntity.getBody());
    }


    private UserDetailsImpl getUserDetails(AppUser appUser) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        return UserDetailsImpl.build(appUser);
    }


    private AppUser createAppUser() {
        String firstName = "Jan";
        String surname = "Kowalski";
        String password = "pa$$w0rd";
        String email = "jankowalski@gmail.com";
        String username = "jan.kowalski";
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleUserEnum.USER));

        return new AppUser(2L, firstName, username, surname, password, email, roles);
    }
}