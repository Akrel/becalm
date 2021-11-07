package com.psk.becalm.transport.converters;

import com.psk.becalm.model.entities.ToDoTask;
import com.psk.becalm.transport.dto.todo.ToDoTaskDto;

public class TodoTaskConverter extends DtoConverter<ToDoTask, ToDoTaskDto> {
    @Override
    protected ToDoTask convertToEntity(ToDoTaskDto toDoTaskDto) {

        ToDoTask toDoTask = new ToDoTask();
        toDoTask.setDescription(toDoTaskDto.text());
        toDoTask.setDone(toDoTaskDto.done());
        return toDoTask;
    }

    @Override
    protected ToDoTaskDto convertToDto(ToDoTask toDoTask) {

        return new ToDoTaskDto()
                .done(toDoTask.isDone())
                .uid(toDoTask.getTaskId().toString())
                .text(toDoTask.getDescription());
    }


    public static ToDoTaskDto toDto(ToDoTask toDoTask) {
        return new TodoTaskConverter().convertToDto(toDoTask);
    }

    public static ToDoTask toEntity(ToDoTaskDto toDoTaskDto) {
        return new TodoTaskConverter().convertToEntity(toDoTaskDto);
    }
}
