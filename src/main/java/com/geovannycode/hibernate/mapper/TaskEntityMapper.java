package com.geovannycode.hibernate.mapper;

import com.geovannycode.hibernate.dto.TaskDTO;
import com.geovannycode.hibernate.model.Task;

import java.util.function.Function;

public class TaskEntityMapper implements Function<TaskDTO, Task> {

    @Override
    public Task apply(TaskDTO taskDTO) {
        Task task = new Task();
        task.setId(taskDTO.id());
        task.setUserId(taskDTO.userId());
        task.setContent(taskDTO.content());
        task.setCompleted(taskDTO.completed());
        task.setCreatedAt(taskDTO.createdAt());
        return task;
    }
}
