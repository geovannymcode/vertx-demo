package com.geovannycode.hibernate.service;

import com.geovannycode.hibernate.dto.TaskDTO;
import com.geovannycode.hibernate.model.TasksList;
import io.vertx.core.Future;

import java.util.Optional;

public interface TaskService {
    Future<TaskDTO> createTask (TaskDTO task);

    Future<TaskDTO> updateTask(TaskDTO task);

    Future<Optional<TaskDTO>> findTaskById (Integer id);

    Future<Void> removeTask (Integer id);

    Future<TasksList> findTasksByUser (Integer userId);
}
