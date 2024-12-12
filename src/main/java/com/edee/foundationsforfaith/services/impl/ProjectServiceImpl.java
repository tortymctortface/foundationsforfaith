package com.edee.foundationsforfaith.services.impl;

import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.repositories.ProjectRepository;
import com.edee.foundationsforfaith.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public abstract class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project getDefensiveProjectByProjectName(String projectName) {
        Optional<Project> project = projectRepository.findProjectByProjectName(projectName);
        if(project.isPresent()){
            return new Project(project.get().getProjectId(),
                    project.get().getProjectName(), project.get().getProjectType(), project.get().getAmountOfFundingRequired());
        }
        return null;
    }



}
