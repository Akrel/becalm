package com.psk.becalm.transport.dto.todo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class ToDoTaskDto {

    private boolean done;

    private String text;

    private String uid;

}

