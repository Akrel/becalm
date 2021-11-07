package com.psk.becalm.transport.dto.todo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
public class ToDoTaskDto {

    private boolean done;

    private String text;

    private String uid;

}

