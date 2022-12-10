package com.geovannycode.hibernate.mapper;

import com.geovannycode.hibernate.dto.TaskDTO;
import com.geovannycode.hibernate.model.Task;

import java.util.function.Function;

public class TaskDTOMapper implements Function<Task, TaskDTO> {

    @Override
    public TaskDTO apply(Task task) {
        return new TaskDTO(task.getId(), task.getUserId(), task.getContent(), task.isCompleted(), task.getCreatedAt());
    }
}
