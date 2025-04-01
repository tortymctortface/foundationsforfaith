package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.ProjectUpdateCreationDto;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.ProjectUpdate;
import com.edee.foundationsforfaith.entities.actions.ProjectAction;

public interface ProjectUpdateService {
    ProjectUpdate createProjectUpdate(ProjectUpdateCreationDto projectUpdateCreationDto);

    void handleAction(ProjectAction action, Project project);
}
