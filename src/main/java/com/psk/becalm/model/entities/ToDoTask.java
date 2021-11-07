package com.psk.becalm.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class ToDoTask extends Task {

    @ColumnDefault(value = "false")
    boolean done;


    @ManyToOne
    private AppUser appUser;
}
