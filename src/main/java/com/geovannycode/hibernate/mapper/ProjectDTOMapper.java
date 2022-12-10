package com.geovannycode.hibernate.mapper;

import com.geovannycode.hibernate.dto.ProjectDTO;
import com.geovannycode.hibernate.model.Project;

import java.util.function.Function;

public class ProjectDTOMapper implements Function<Project, ProjectDTO> {
    @Override
    public ProjectDTO apply(Project project) {
        return new ProjectDTO(project.getId(), project.getUserId(), project.getName());
    }
}
