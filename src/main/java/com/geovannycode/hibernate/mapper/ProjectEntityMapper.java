package com.geovannycode.hibernate.mapper;

import com.geovannycode.hibernate.dto.ProjectDTO;
import com.geovannycode.hibernate.model.Project;

import java.util.function.Function;

public class ProjectEntityMapper implements Function<ProjectDTO, Project> {
    @Override
    public Project apply(ProjectDTO projectDTO) {
        Project entity = new Project();
        entity.setId(projectDTO.id());
        entity.setUserId(projectDTO.userId());
        entity.setName(projectDTO.name());
        return entity;
    }
}
