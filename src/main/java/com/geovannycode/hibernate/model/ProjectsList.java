package com.geovannycode.hibernate.model;

import com.geovannycode.hibernate.dto.ProjectDTO;

import java.util.List;

public record ProjectsList(List<ProjectDTO> projects) {
}
