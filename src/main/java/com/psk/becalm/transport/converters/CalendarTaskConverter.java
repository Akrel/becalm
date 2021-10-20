package com.psk.becalm.transport.converters;

import com.psk.becalm.model.entities.CalendarTask;
import com.psk.becalm.transport.dto.request.calendar.CalendarTaskDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class CalendarTaskConverter extends DtoConverter<CalendarTask, CalendarTaskDto> {
    private final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @SneakyThrows
    @Override
    protected CalendarTask convertToEntity(CalendarTaskDto addCalendarTaskRequest) {


        LocalDateTime endTime = addCalendarTaskRequest.getEnd() == null ? null : ZonedDateTime.parse(addCalendarTaskRequest.getEnd()).toLocalDateTime();

        LocalDateTime startTime = ZonedDateTime.parse(addCalendarTaskRequest.getStart()).toLocalDateTime();

        CalendarTask calendarTask = new CalendarTask();
        calendarTask.setColor(addCalendarTaskRequest.getColor());
        calendarTask.setEndDate(endTime);
        calendarTask.setStartDate(startTime);
        calendarTask.setDescription(addCalendarTaskRequest.getDescription());
        calendarTask.setTittle(addCalendarTaskRequest.getName());
        calendarTask.setAllDay(addCalendarTaskRequest.isAllDay());
        return calendarTask;
    }

    @Override
    protected CalendarTaskDto convertToDto(CalendarTask calendarTask) {
        CalendarTaskDto addCalendarTaskDto = new CalendarTaskDto();
        addCalendarTaskDto.setAllDay(calendarTask.isAllDay());
        addCalendarTaskDto.setName(calendarTask.getTittle());
        addCalendarTaskDto.setEnd(calendarTask.getEndDate() != null ? calendarTask.getEndDate().format(dateFormat) : "");
        addCalendarTaskDto.setStart(calendarTask.getStartDate().format(dateFormat));
        addCalendarTaskDto.setColor(calendarTask.getColor());
        addCalendarTaskDto.setDescription(calendarTask.getDescription());
        addCalendarTaskDto.setTaskUuid(calendarTask.getTaskId().toString());
        return addCalendarTaskDto;
    }

    public static CalendarTaskDto toDto(CalendarTask calendarTask) {
        return new CalendarTaskConverter().convertToDto(calendarTask);
    }

    public static CalendarTask toEntity(CalendarTaskDto addCalendarTaskRequest) {
        return new CalendarTaskConverter().convertToEntity(addCalendarTaskRequest);
    }

}
