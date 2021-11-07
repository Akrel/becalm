package com.psk.becalm.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
@AllArgsConstructor
public abstract class Task {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID taskId;

    private String description;

    public Task() {
        this.taskId = UUID.randomUUID();
    }

}
