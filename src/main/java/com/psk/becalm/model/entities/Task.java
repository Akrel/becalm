package com.psk.becalm.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Task {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID taskId;

    private String description;

    public Task() {
        this.taskId = UUID.randomUUID();
    }

}
