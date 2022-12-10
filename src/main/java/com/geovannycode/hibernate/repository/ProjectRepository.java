package com.geovannycode.hibernate.repository;

import com.geovannycode.hibernate.dto.ProjectDTO;
import com.geovannycode.hibernate.model.ProjectsList;
import io.vertx.core.Future;

import java.util.Optional;

public interface ProjectRepository {
    Future<ProjectDTO> createProject (ProjectDTO projectDTO);

    Future<ProjectDTO> updateProject(ProjectDTO projectDTO);

    Future<Optional<ProjectDTO>> findProjectById (Integer id);

    Future<Void> removeProject (Integer id);

    Future<ProjectsList> findProjectsByUser (Integer userId);
}
