package com.psk.becalm.model.entities;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ToDoTask extends Task {

    @ColumnDefault(value = "false")
    boolean done;


    @ManyToOne
    private AppUser appUser;
}
