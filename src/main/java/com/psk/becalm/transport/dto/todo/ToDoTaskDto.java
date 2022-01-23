package com.psk.becalm.transport.dto.todo;

import lombok.*;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ToDoTaskDto {

    private boolean done;

    private String text;

    private String uid;

}

