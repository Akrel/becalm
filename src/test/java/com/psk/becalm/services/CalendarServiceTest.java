package com.psk.becalm.services;

import com.psk.becalm.exceptions.UserException;
import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.CalendarTask;
import com.psk.becalm.model.entities.Role;
import com.psk.becalm.model.entities.RoleUserEnum;
import com.psk.becalm.model.repository.CalendarTaskRepository;
import com.psk.becalm.transport.converters.CalendarTaskConverter;
import com.psk.becalm.transport.dto.calendar.CalendarTaskDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

class CalendarServiceTest {

    private CalendarTaskRepository calendarTaskRepository;

    private AppUserService appUserService;
    private CalendarService calendarService;

    @BeforeEach
    private void init() {
        calendarTaskRepository = Mockito.mock(CalendarTaskRepository.class);
        appUserService = Mockito.mock(AppUserService.class);
        calendarService = new CalendarService(calendarTaskRepository, appUserService);
    }


    @Test
    void addTaskForUserTest() throws UserException {
        AppUser appUser = createAppUser();
        Mockito.when(appUserService.getAppUserById(appUser.getUserId())).thenReturn(appUser);
        CalendarTaskDto taskDto = new CalendarTaskDto("Task1", "2021-12-22T02:06:58.147Z", "2021-12-26T20:00:00.00Z", "Task description", false, "red", null);
        CalendarTask calendarTask = CalendarTaskConverter.toEntity(taskDto);
        calendarTask.setAppUser(appUser);

        calendarService.addTaskForUser(taskDto, appUser.getUserId());

        Mockito.verify(calendarTaskRepository, Mockito.times(1)).save(Mockito.eq(calendarTask));
    }

    @Test
    void removeUserTask() throws UserException {
        AppUser appUser = createAppUser();
        appUserService.getAppUserById(appUser.getUserId());
        Mockito.when(appUserService.getAppUserById(appUser.getUserId())).thenReturn(appUser);
        CalendarTaskDto taskDto = new CalendarTaskDto("Task1", "2021-12-22T02:06:58.147Z", "2021-12-26T20:00:00.00Z", "Task description", false, "red", UUID.randomUUID().toString());
        CalendarTask calendarTask = CalendarTaskConverter.toEntity(taskDto);
        calendarTask.setAppUser(appUser);

        Mockito.when(calendarTaskRepository.findByTaskIdAndAppUser(calendarTask.getTaskId(), appUser)).thenReturn(Optional.of(calendarTask));

        calendarService.removeUserTask(calendarTask.getTaskId().toString(), appUser.getUserId());

        Mockito.verify(calendarTaskRepository, Mockito.times(1)).deleteById(Mockito.eq(calendarTask.getTaskId()));
    }

    @Test
    void editCalendarTaskTest() throws UserException {
        //given
        AppUser appUser = createAppUser();
        appUserService.getAppUserById(appUser.getUserId());

        Mockito.when(appUserService.getAppUserById(appUser.getUserId())).thenReturn(appUser);
        String taskUUID = UUID.randomUUID().toString();
        CalendarTaskDto taskDto = new CalendarTaskDto("Task1", "2021-12-22T02:06:58.147Z", "2021-12-26T20:00:00.00Z", "Task description", false, "red", taskUUID);
        CalendarTaskDto taskDTOToEdit = new CalendarTaskDto("Zadanie 1", "2021-12-22T02:06:58.147Z", "2021-12-26T20:00:00.00Z", "Opis zadania", false, "red", taskUUID);
        CalendarTask calendarTask = CalendarTaskConverter.toEntity(taskDto);
        calendarTask.setAppUser(appUser);
        calendarTask.setTaskId(UUID.fromString(taskUUID));
        Mockito.when(calendarTaskRepository.findByTaskIdAndAppUser(calendarTask.getTaskId(), appUser)).thenReturn(Optional.of(calendarTask));

        //when
        calendarService.editCalendarTask(taskDTOToEdit, appUser.getUserId());

        //then
        CalendarTaskDto expectedTaskDto = new CalendarTaskDto("Zadanie 1", "2021-12-22T02:06:58.147Z", "2021-12-26T20:00:00.00Z", "Opis zadania", false, "red", UUID.randomUUID().toString());
        CalendarTask expectedTask = CalendarTaskConverter.toEntity(expectedTaskDto);
        expectedTask.setAppUser(appUser);
        expectedTask.setTaskId(UUID.fromString(taskUUID));
        Mockito.verify(calendarTaskRepository, Mockito.times(1)).save(Mockito.eq(expectedTask));
    }

    @Test
    void findCalendarTaskInMonth() throws UserException {
        //given
        AppUser appUser = createAppUser();
        appUserService.getAppUserById(appUser.getUserId());

        Mockito.when(appUserService.getAppUserById(appUser.getUserId())).thenReturn(appUser);
        CalendarTaskDto taskDto = new CalendarTaskDto("Task1", "2021-12-22T02:06:58.147Z", "2021-12-26T20:00:00.00Z", "Task description", false, "red", UUID.randomUUID().toString());
        CalendarTask calendarTask = CalendarTaskConverter.toEntity(taskDto);
        calendarTask.setAppUser(appUser);
        //when
        calendarService.findCalendarTaskInMonth(appUser.getUserId(), 12);

        //then
        Mockito.verify(calendarTaskRepository, Mockito.times(1)).findAllByAppUser(Mockito.eq(appUser));
    }


    private AppUser createAppUser() {
        String firstName = "Jan";
        String surname = "Kowalski";
        String password = "pa$$w0rd";
        String email = "jankowalski@gmail.com";
        String username = "jan.kowalski";
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(RoleUserEnum.USER));

        return new AppUser(null, firstName, username, surname, password, email, roles);
    }

}