package com.psk.becalm.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public abstract class Task {
    @Id
    @GeneratedValue(generator = "UUID")
    private final UUID taskId = UUID.randomUUID();

    private String description;

}
