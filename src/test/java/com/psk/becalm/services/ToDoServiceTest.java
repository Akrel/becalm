package com.psk.becalm.services;

import com.psk.becalm.exceptions.UserException;
import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import com.psk.becalm.model.entities.ToDoTask;
import com.psk.becalm.model.repository.ToDoTaskRepository;
import com.psk.becalm.model.repository.UserRepository;
import com.psk.becalm.transport.converters.TodoTaskConverter;
import com.psk.becalm.transport.dto.todo.ToDoTaskDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

class ToDoServiceTest {

    private ToDoTaskRepository toDoTaskRepository;

    private AppUserService appUserService;

    private ToDoService toDoService;
    private UserRepository userRepository;

    @BeforeEach
    private void init() {
        toDoTaskRepository = Mockito.mock(ToDoTaskRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        appUserService = new AppUserService(userRepository);
        toDoService = new ToDoService(appUserService, toDoTaskRepository);
    }

    @Test
    void getAllTodoTask() {
    }

    @Test
    void addNewTodoTaskTest() throws UserException {
        //given
        AppUser appUser = createAppUser();
        Mockito.when(userRepository.findById(appUser.getUserId())).thenReturn(Optional.of(appUser));
        ToDoTaskDto taskDto = new ToDoTaskDto(true, "Kupić mleko", UUID.randomUUID().toString());

        //when
        toDoService.addNewTodoTask(appUser.getUserId(), taskDto);

        //then
        ToDoTask toDoTask = TodoTaskConverter.toEntity(taskDto);
        toDoTask.setAppUser(appUser);

        Mockito.verify(toDoTaskRepository, Mockito.times(1)).save(Mockito.eq(toDoTask));
    }

    @Test
    void editTodoTaskTest() {
        //given
        AppUser appUser = createAppUser();
        ToDoTaskDto taskDto = new ToDoTaskDto(true, "Kupic mleko, kupic jajka", UUID.randomUUID().toString());
        ToDoTask toDoTask = new ToDoTask(true, appUser);
        toDoTask.setDescription("Kupic mleko");
        toDoTask.setTaskId(UUID.fromString(taskDto.getUid()));
        Mockito.when(toDoTaskRepository.getById(toDoTask.getTaskId())).thenReturn(toDoTask);

        //when
        toDoService.editTodoTask(taskDto);

        //then
        ToDoTask expectedToDoTask = new ToDoTask(true, appUser);
        toDoTask.setDescription("Kupic mleko, kupic jajka");
        toDoTask.setTaskId(UUID.fromString(taskDto.getUid()));
        Mockito.verify(toDoTaskRepository, Mockito.times(1)).save(Mockito.eq(expectedToDoTask));
    }

    @Test
    void removeToDoTaskTest() {
        //given
        AppUser appUser = createAppUser();
        ToDoTaskDto taskDto = new ToDoTaskDto(true, "Kupic mleko, kupic jajka", null);
        ToDoTask toDoTask = TodoTaskConverter.toEntity(taskDto);
        toDoTask.setAppUser(appUser);
        Mockito.when(userRepository.findById(appUser.getUserId())).thenReturn(Optional.of(appUser));
        Mockito.when(toDoTaskRepository.findToDoTaskByAppUserAndTaskId(appUser, toDoTask.getTaskId())).thenReturn(Optional.of(toDoTask));

        //when
        toDoService.removeToDoTask(appUser.getUserId(), toDoTask.getTaskId().toString());

        //then
        Mockito.verify(toDoTaskRepository, Mockito.times(1)).delete(Mockito.eq(toDoTask));
    }

    @Test
    void toggleTaskTodoTest() {
        //given
        AppUser appUser = createAppUser();
        ToDoTaskDto taskDto = new ToDoTaskDto(true, "Kupic mleko, kupic jajka", null);
        ToDoTask toDoTask = TodoTaskConverter.toEntity(taskDto);
        toDoTask.setAppUser(appUser);
        Mockito.when(userRepository.findById(appUser.getUserId())).thenReturn(Optional.of(appUser));
        Mockito.when(toDoTaskRepository.findToDoTaskByAppUserAndTaskId(appUser, toDoTask.getTaskId())).thenReturn(Optional.of(toDoTask));


        //when
        toDoService.toggleTaskTodo(appUser.getUserId(), toDoTask.getTaskId().toString());

        ToDoTask expectedToDoTask = new ToDoTask(false, appUser);
        expectedToDoTask.setDescription("Kupic mleko, kupic jajka");


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