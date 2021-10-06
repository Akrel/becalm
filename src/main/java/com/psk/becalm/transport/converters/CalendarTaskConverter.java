package com.psk.becalm.transport.converters;

import com.psk.becalm.model.entities.AppUser;
import com.psk.becalm.model.entities.CalendarTask;
import com.psk.becalm.transport.dto.model.AppUserDto;
import com.psk.becalm.transport.dto.request.calendar.AddCalendarTaskDto;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;

public class CalendarTaskConverter extends DtoConverter<CalendarTask, AddCalendarTaskDto> {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");


    @SneakyThrows
    @Override
    protected CalendarTask convertToEntity(AddCalendarTaskDto addCalendarTaskRequest) {
        CalendarTask calendarTask = new CalendarTask();
        calendarTask.setColor(addCalendarTaskRequest.getColor());
        calendarTask.setEndDate(dateFormat.parse(addCalendarTaskRequest.getEndDate()));
        calendarTask.setStartDate(dateFormat.parse(addCalendarTaskRequest.getEndDate()));
        calendarTask.setDescription(addCalendarTaskRequest.getDescription());
        calendarTask.setTittle(addCalendarTaskRequest.getTittle());
        calendarTask.setAllDay(addCalendarTaskRequest.isAllDay());
        return calendarTask;
    }

    @Override
    protected AddCalendarTaskDto convertToDto(CalendarTask calendarTask) {
        AddCalendarTaskDto addCalendarTaskDto = new AddCalendarTaskDto();
        addCalendarTaskDto.setTaskId(calendarTask.getTaskId().toString());
        addCalendarTaskDto.setAllDay(calendarTask.isAllDay());
        addCalendarTaskDto.setTittle(calendarTask.getTittle());
        addCalendarTaskDto.setEndDate(calendarTask.getEndDate().toString());
        addCalendarTaskDto.setStartDate(calendarTask.getStartDate().toString());
        addCalendarTaskDto.setDescription(calendarTask.getDescription());
        return addCalendarTaskDto;
    }
    public static AddCalendarTaskDto toDto(CalendarTask calendarTask) {
        return new CalendarTaskConverter().convertToDto(calendarTask);
    }

    public static CalendarTask toEntity(AddCalendarTaskDto addCalendarTaskRequest) {
        return new CalendarTaskConverter().convertToEntity(addCalendarTaskRequest);
    }

}
