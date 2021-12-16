package com.psk.becalm.transport.converters;

import com.psk.becalm.model.entities.ToDoTask;
import com.psk.becalm.transport.dto.todo.ToDoTaskDto;

public class TodoTaskConverter extends DtoConverter<ToDoTask, ToDoTaskDto> {
    @Override
    protected ToDoTask convertToEntity(ToDoTaskDto toDoTaskDto) {

        ToDoTask toDoTask = new ToDoTask();
        toDoTask.setDescription(toDoTaskDto.getText());
        toDoTask.setDone(toDoTaskDto.isDone());
        return toDoTask;
    }

    @Override
    protected ToDoTaskDto convertToDto(ToDoTask toDoTask) {

        return new ToDoTaskDto()
                .setDone(toDoTask.isDone())
                .setUid(toDoTask.getTaskId().toString())
                .setText(toDoTask.getDescription());
    }


    public static ToDoTaskDto toDto(ToDoTask toDoTask) {
        return new TodoTaskConverter().convertToDto(toDoTask);
    }

    public static ToDoTask toEntity(ToDoTaskDto toDoTaskDto) {
        return new TodoTaskConverter().convertToEntity(toDoTaskDto);
    }
}
