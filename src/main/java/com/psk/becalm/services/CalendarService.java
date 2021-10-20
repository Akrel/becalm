package com.psk.becalm.services;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.CalendarTask;
import com.psk.becalm.model.repository.CalendarTaskRepository;
import com.psk.becalm.transport.converters.CalendarTaskConverter;
import com.psk.becalm.transport.dto.request.calendar.CalendarTaskDto;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
    public List<CalendarTask> findCalendarTaskInMonth(Long idUser, int month) {
        AppUser appUserById = appUserService.getAppUserById(idUser);

        return calendarTaskRepository.findAllByAppUser(appUserById);
    }

}
