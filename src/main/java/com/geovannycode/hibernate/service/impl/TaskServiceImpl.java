package com.geovannycode.hibernate.service.impl;

import com.geovannycode.hibernate.dto.TaskDTO;
import com.geovannycode.hibernate.model.TasksList;
import com.geovannycode.hibernate.repository.TaskRepository;
import com.geovannycode.hibernate.service.TaskService;
import io.vertx.core.Future;

import java.util.Optional;

public record TaskServiceImpl(TaskRepository repository) implements TaskService {
    @Override
    public Future<TaskDTO> createTask(TaskDTO task) {
        return repository.createTask(task);
    }

    @Override
    public Future<TaskDTO> updateTask(TaskDTO task) {
        return repository.updateTask(task);
    }

    @Override
    public Future<Optional<TaskDTO>> findTaskById(Integer id) {
        return repository.findTaskById(id);
    }

    @Override
    public Future<Void> removeTask(Integer id) {
        return repository.removeTask(id);
    }

    @Override
    public Future<TasksList> findTasksByUser(Integer userId) {
        return repository.findTasksByUser(userId);
    }
}
