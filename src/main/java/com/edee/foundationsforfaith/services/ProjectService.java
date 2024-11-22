package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.ProjectCreationDto;
import com.edee.foundationsforfaith.entities.Project;
import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> getAllProjects();
    Optional<Project> getProjectByProjectName(String projectName);
    Project createProject(ProjectCreationDto projectCreationDto);
}
