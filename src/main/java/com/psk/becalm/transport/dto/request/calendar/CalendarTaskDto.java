package com.psk.becalm.transport.dto.request.calendar;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CalendarTaskDto {

    private String name;

    private String start;

    private String end;

    private String description;

    private boolean allDay;

    private String color;

    private String taskUuid;
}
