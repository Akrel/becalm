package com.psk.becalm.controllers;

import com.psk.becalm.exceptions.UserException;
import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.CalendarTask;
import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import com.psk.becalm.services.CalendarService;
import com.psk.becalm.services.UserDetailsImpl;
import com.psk.becalm.transport.converters.CalendarTaskConverter;
import com.psk.becalm.transport.dto.calendar.CalendarTaskDto;
import com.psk.becalm.transport.dto.response.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CalendarApiTest {

    private CalendarService calendarService;

    private CalendarApi calendarApi;

    @BeforeEach
    private void init() {

        calendarService = Mockito.mock(CalendarService.class);
        calendarApi = new CalendarApi(calendarService);
    }


    @Test
    void getAllTaskInMonthTest() {
        AppUser appUser = createAppUser();
        UserDetailsImpl build = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(build);

        CalendarTask calendarTask = new CalendarTask("Zadanie tytuł", LocalDateTime.of(2020, 12, 4, 12, 10, 5), LocalDateTime.of(2020, 12, 8, 16, 10, 5), false, "red", appUser);
        CalendarTask calendarTask1 = new CalendarTask("Zadanie tytuł2", LocalDateTime.of(2020, 12, 1, 6, 10, 5), LocalDateTime.of(2020, 12, 9, 12, 10, 5), false, "yellow", appUser);
        ArrayList<CalendarTask> calendarTasks = new ArrayList<>();
        calendarTasks.add(calendarTask);
        calendarTasks.add(calendarTask1);
        Mockito.when(calendarService.findCalendarTaskInMonth(appUser.getUserId(), Integer.parseInt("12"))).thenReturn(calendarTasks);


        ResponseEntity<List<CalendarTaskDto>> responseEntity = calendarApi.getAllTaskInMonth("12");


        ResponseEntity<List<CalendarTaskDto>> expectedResponse = ResponseEntity.ok(calendarTasks.stream().map(CalendarTaskConverter::toDto).collect(Collectors.toList()));

        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(Objects.requireNonNull(expectedResponse.getBody()).size(), responseEntity.getBody().size());
    }


    @Test
    void getAllTaskInMonthErrorTest() {
        AppUser appUser = createAppUser();
        UserDetailsImpl build = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(build);
        Mockito.when(calendarService.findCalendarTaskInMonth(appUser.getUserId(), Integer.parseInt("13"))).thenReturn(new ArrayList<>());

        ResponseEntity<List<CalendarTaskDto>> responseEntity = calendarApi.getAllTaskInMonth("13");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void editCalendarTask() {
        AppUser appUser = createAppUser();
        UserDetailsImpl build = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(build);
        UUID uuid = UUID.randomUUID();
        CalendarTaskDto taskDto = new CalendarTaskDto("Task 1", LocalDateTime.of(2020, 12, 4, 12, 10, 5).toString(), LocalDateTime.of(2020, 12, 9, 12, 10, 5).toString(), "Opis", false, "green", uuid.toString());
        Mockito.when(calendarService.editCalendarTask(taskDto, appUser.getUserId())).thenReturn(null);

        ResponseEntity<?> responseEntity = calendarApi.editTask(taskDto);


        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }


    @Test
    void editCalendarErrorTask() {
        AppUser appUser = createAppUser();
        UserDetailsImpl build = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(build);
        UUID uuid = UUID.randomUUID();
        CalendarTaskDto taskDto = new CalendarTaskDto("Task 1", "2021-03-23T15:00:00Z", "2018-03-23T15:00:00Z", "Opis", false, "green", uuid.toString());
        CalendarTask calendarTask = CalendarTaskConverter.toEntity(taskDto);
        Mockito.when(calendarService.editCalendarTask(taskDto, appUser.getUserId())).thenReturn(calendarTask);

        ResponseEntity<?> responseEntity = calendarApi.editTask(taskDto);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void addCalendarTaskTest() {
        AppUser appUser = createAppUser();
        UserDetailsImpl build = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(build);
        UUID uuid = UUID.randomUUID();
        CalendarTaskDto taskDto = new CalendarTaskDto("Task 1", "2021-03-23T15:00:00Z", "2018-03-23T15:00:00Z", "Opis", false, "green", uuid.toString());
        CalendarTask calendarTask = CalendarTaskConverter.toEntity(taskDto);
        Mockito.when(calendarService.addTaskForUser(taskDto, appUser.getUserId())).thenReturn(calendarTask);

        ResponseEntity<CalendarTask> calendarTaskResponseEntity = calendarApi.addNewTask(taskDto);

        assertEquals(HttpStatus.OK, calendarTaskResponseEntity.getStatusCode());
        assertEquals(calendarTask, calendarTaskResponseEntity.getBody());

    }


    @Test
    void addCalendarTaskErrorTest() {
        AppUser appUser = createAppUser();
        UserDetailsImpl build = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(build);
        UUID uuid = UUID.randomUUID();
        CalendarTaskDto taskDto = new CalendarTaskDto("Task 1", "2021-03-23T15:00:00Z", "2018-03-23T15:00:00Z", "Opis", false, "green", uuid.toString());
        CalendarTask calendarTask = CalendarTaskConverter.toEntity(taskDto);
        Mockito.when(calendarService.addTaskForUser(taskDto, appUser.getUserId())).thenReturn(null);

        ResponseEntity<CalendarTask> calendarTaskResponseEntity = calendarApi.addNewTask(taskDto);

        assertEquals(HttpStatus.BAD_REQUEST, calendarTaskResponseEntity.getStatusCode());
        assertNull(calendarTaskResponseEntity.getBody());

    }

    @Test
    void removeCalendarTask() throws UserException {
        AppUser appUser = createAppUser();
        UserDetailsImpl build = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(build);
        UUID uuid = UUID.randomUUID();

        Mockito.doNothing().when(calendarService).removeUserTask("12", appUser.getUserId());

        calendarApi.removeTask("12");

    }


    @Test
    void removeCalendarErrorTask() throws UserException {
        AppUser appUser = createAppUser();
        UserDetailsImpl build = getUserDetails(appUser);
        Mockito.when((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(build);
        UUID uuid = UUID.randomUUID();

        Mockito.doThrow(new UserException(String.format("Cannot find calendar task with id %s", uuid))).when(calendarService).removeUserTask("12", appUser.getUserId());


        ResponseEntity<?> responseEntity = calendarApi.removeTask("12");


        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(new MessageResponse("Cannot remove with id: 12"), responseEntity.getBody());
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