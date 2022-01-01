package com.psk.becalm.model.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;


@Entity
@Setter()
@Getter
@EqualsAndHashCode
public class CalendarTask extends Task {

    private String tittle;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @ColumnDefault("true")
    private boolean allDay = true;

    private String color;

    @ManyToOne
    private AppUser appUser;
}
