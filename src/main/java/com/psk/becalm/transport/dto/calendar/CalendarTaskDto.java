package com.psk.becalm.transport.dto.calendar;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(fluent = true, chain = true)
public class CalendarTaskDto {

    private String name;

    private String start;

    private String end;

    private String description;

    private boolean allDay;

    private String color;

    private String taskUuid;
}
