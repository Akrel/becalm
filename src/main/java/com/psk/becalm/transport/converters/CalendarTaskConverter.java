package com.psk.becalm.transport.converters;

import com.psk.becalm.model.entities.CalendarTask;
import com.psk.becalm.transport.dto.calendar.CalendarTaskDto;
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

        LocalDateTime endTime = addCalendarTaskRequest.end() == null ? null : ZonedDateTime.parse(addCalendarTaskRequest.end()).toLocalDateTime();
        LocalDateTime startTime = ZonedDateTime.parse(addCalendarTaskRequest.start()).toLocalDateTime();

        CalendarTask calendarTask = new CalendarTask();
        calendarTask.setColor(addCalendarTaskRequest.color());
        calendarTask.setEndDate(endTime);
        calendarTask.setStartDate(startTime);
        calendarTask.setDescription(addCalendarTaskRequest.description());
        calendarTask.setTittle(addCalendarTaskRequest.name());
        calendarTask.setAllDay(addCalendarTaskRequest.allDay());
        return calendarTask;
    }

    @Override
    protected CalendarTaskDto convertToDto(CalendarTask calendarTask) {
        CalendarTaskDto addCalendarTaskDto = new CalendarTaskDto();

        addCalendarTaskDto.allDay(calendarTask.isAllDay())
                .name(calendarTask.getTittle())
                .end(calendarTask.getEndDate() != null ? calendarTask.getEndDate().format(dateFormat) : "")
                .start(calendarTask.getStartDate().format(dateFormat))
                .color(calendarTask.getColor())
                .description(calendarTask.getDescription())
                .taskUuid(calendarTask.getTaskId().toString());

        return addCalendarTaskDto;
    }

    public static CalendarTaskDto toDto(CalendarTask calendarTask) {
        return new CalendarTaskConverter().convertToDto(calendarTask);
    }

    public static CalendarTask toEntity(CalendarTaskDto addCalendarTaskRequest) {
        return new CalendarTaskConverter().convertToEntity(addCalendarTaskRequest);
    }

}
