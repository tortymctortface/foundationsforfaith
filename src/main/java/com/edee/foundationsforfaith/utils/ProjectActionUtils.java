package com.edee.foundationsforfaith.utils;

import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.ProjectUpdate;
import com.edee.foundationsforfaith.entities.actions.*;
import com.edee.foundationsforfaith.enums.ProgressStatus;
import com.edee.foundationsforfaith.enums.ProjectUpdateType;
import com.edee.foundationsforfaith.repositories.ProjectRepository;
import com.edee.foundationsforfaith.repositories.ProjectUpdateRepository;

import java.time.LocalDateTime;

public class ProjectActionUtils{

    private final ProjectUpdateRepository projectUpdateRepository;
    private final ProjectRepository projectRepository;

    public ProjectActionUtils(ProjectUpdateRepository updates, ProjectRepository projects) {
        this.projectUpdateRepository = updates;
        this.projectRepository = projects;
    }

    public static void handleAction(ProjectAction action, Project project) {
        String description;
        switch (action) {
            case StartProject s -> {
                project.setProjectStatus(ProgressStatus.BUILD_IN_PROGRESS);
                description = "The build of  "+project.getProjectName() + " is in progress. Date: " + LocalDateTime.now();
            }
            case PauseProject p -> {
                if (!ProgressStatus.BUILD_IN_PROGRESS.equals(project.getProjectStatus())) {
                    throw new IllegalStateException("Project must be in progress to pause.");
                }
                project.setProjectStatus(ProgressStatus.BUILD_PAUSED);
                description = "The build of  "+project.getProjectName() + " has been paused. We will update you once it re-starts. Date: " + LocalDateTime.now();
            }
            case CompleteProject c -> {
                if (!ProgressStatus.BUILD_IN_PROGRESS.equals(project.getProjectStatus())) {
                    throw new IllegalStateException("Only in-progress projects can be completed.");
                }
                project.setProjectStatus(ProgressStatus.BUILD_COMPLETE);
                description = "The build of  "+project.getProjectName() + " has been completed. We will update you with pictures soon! Date: " + LocalDateTime.now();
            }
            default -> throw new IllegalStateException("Unknown project action");
        }

        ProjectUpdate update = new ProjectUpdate();
        update.setProjectUpdateType(ProjectUpdateType.PROJECT_CHANGING_STATUS);
        update.setDescription(description);
        update.setEmailAllStones(true);
        ProjectUpdate updated = projectUpdateRepository.save(update);

        // Persist changes to project
        project.getProjectUpdateIds().add(updated);
        projectRepository.save(project);
    }
}
