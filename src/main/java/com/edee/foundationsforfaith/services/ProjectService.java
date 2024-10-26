package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.entities.Project;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

public interface ProjectService {
    List<Project> getAllProjects();
    Optional<Project> getProjectByProjectName(String projectName);
}
