package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.NewProjectDto;
import com.edee.foundationsforfaith.entities.Location;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.enums.BuildingType;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> getAllProjects();
    Optional<Project> getProjectByProjectName(String projectName);

    Project createProject(ObjectId location, NewProjectDto newProjectDto);
}
