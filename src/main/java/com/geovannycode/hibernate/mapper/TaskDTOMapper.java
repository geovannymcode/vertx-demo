package com.geovannycode.hibernate.mapper;

import com.geovannycode.hibernate.dto.ProjectDTO;
import com.geovannycode.hibernate.dto.TaskDTO;
import com.geovannycode.hibernate.model.Task;

import java.util.Optional;
import java.util.function.Function;

public class TaskDTOMapper implements Function<Task, TaskDTO> {

    @Override
    public TaskDTO apply(Task task) {
        ProjectDTOMapper mapper = new ProjectDTOMapper();
        Optional<ProjectDTO> project = null;
        if(task.getProject()!=null)
        {
            project = Optional.ofNullable(mapper.apply(task.getProject()));
        }else{
            project = Optional.empty();
        }
        return new TaskDTO(task.getId(), task.getUserId(), task.getContent(), task.isCompleted(), task.getCreatedAt(),project);
    }
}
