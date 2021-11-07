package com.psk.becalm.services;

import com.psk.becalm.exceptions.UserException;
import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.CalendarTask;
import com.psk.becalm.model.repository.CalendarTaskRepository;
import com.psk.becalm.transport.converters.CalendarTaskConverter;
import com.psk.becalm.transport.dto.calendar.CalendarTaskDto;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    @Autowired
    private CalendarTaskRepository calendarTaskRepository;
    @Autowired
    private AppUserService appUserService;

    @SneakyThrows
    public CalendarTask addTaskForUser(CalendarTaskDto addCalendarTaskRequest, Long userId) {

        AppUser appUserById = appUserService.getAppUserById(userId);

        CalendarTask calendarTask = CalendarTaskConverter.toEntity(addCalendarTaskRequest);
        calendarTask.setAppUser(appUserById);
        return calendarTaskRepository.save(calendarTask);
    }

    public void removeUserTask(String taskUuid, Long userId) throws UserException {
        AppUser appUserById = appUserService.getAppUserById(userId);

        calendarTaskRepository.findByTaskIdAndAppUser(UUID.fromString(taskUuid), appUserById).
                orElseThrow(() -> new UserException(String.format("Cannot find calendar task with id %s", taskUuid)));

        calendarTaskRepository.deleteById(UUID.fromString(taskUuid));
    }

    @SneakyThrows
    public CalendarTask editCalendarTask(CalendarTaskDto taskDto, Long userID) {
        CalendarTask calendarTask = CalendarTaskConverter.toEntity(taskDto);
        AppUser appUserById = appUserService.getAppUserById(userID);
        calendarTask.setAppUser(appUserById);
        calendarTask.setTaskId(UUID.fromString(taskDto.taskUuid()));
        return calendarTaskRepository.save(calendarTask);
    }


    @SneakyThrows
    public List<CalendarTask> findCalendarTaskInMonth(Long idUser, int month) {
        AppUser appUserById = appUserService.getAppUserById(idUser);
        return calendarTaskRepository.findAllByAppUser(appUserById).stream()
                .filter(el -> el.getStartDate().getMonth().equals(Month.of(month)))
                .collect(Collectors.toList());
    }

}
