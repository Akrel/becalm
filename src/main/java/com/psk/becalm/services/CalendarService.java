package com.psk.becalm.services;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.CalendarTask;
import com.psk.becalm.model.repository.CalendarTaskRepository;
import com.psk.becalm.transport.converters.CalendarTaskConverter;
import com.psk.becalm.transport.dto.request.calendar.AddCalendarTaskDto;
import com.psk.becalm.transport.dto.request.calendar.RemoveCalendarTaskDto;
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
    public CalendarTask addTaskForUser(AddCalendarTaskDto addCalendarTaskRequest) {

        AppUser appUserById = appUserService.getAppUserById(addCalendarTaskRequest.getUserId());

        CalendarTask calendarTask = CalendarTaskConverter.toEntity(addCalendarTaskRequest);
        calendarTask.setAppUser(appUserById);

        return calendarTaskRepository.save(calendarTask);
    }


    @SneakyThrows
    public void removeUserTask(RemoveCalendarTaskDto removeCalendarTaskDto) {
        AppUser appUserById = appUserService.getAppUserById(removeCalendarTaskDto.getUserId());

        calendarTaskRepository.findByIdAndAppUser(UUID.fromString(removeCalendarTaskDto.getTaskId()), appUserById).
                orElseThrow(() -> new UserException(String.format("Cannot find calendar task with id %s", removeCalendarTaskDto.getTaskId())));

        calendarTaskRepository.deleteById(UUID.fromString(removeCalendarTaskDto.getTaskId()));
    }

    @SneakyThrows
    public List<CalendarTask> findCalendarTaskInMonth(Long idUser, String month) {
        AppUser appUserById = appUserService.getAppUserById(idUser);

        return calendarTaskRepository.findAllByAppUserAndStartDate_Month(appUserById, Integer.getInteger(month));
    }

}
