package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.ProjectUpdateCreationDto;
import com.edee.foundationsforfaith.entities.ProjectUpdate;

public interface ProjectUpdateService {
    ProjectUpdate createProjectUpdate(ProjectUpdateCreationDto projectUpdateCreationDto);
}
