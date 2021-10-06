package com.psk.becalm.transport.dto.request.calendar;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Setter
@Getter
public class AddCalendarTaskDto {

    private Long userId;

    private String taskId;

    private String tittle;

    private String startDate;

    private String endDate;

    private String description;

    @ColumnDefault("true")
    private boolean allDay = true;

    private String color;
}
