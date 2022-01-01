package com.psk.becalm.transport.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CalendarTaskDto {

    private String name;

    private String start;

    private String end;

    private String description;

    private boolean allDay;

    private String color;

    private String taskUuid;
}
