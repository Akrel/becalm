package com.psk.becalm.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Setter
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
