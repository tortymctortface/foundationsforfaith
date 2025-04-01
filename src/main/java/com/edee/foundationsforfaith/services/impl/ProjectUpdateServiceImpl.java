package com.edee.foundationsforfaith.services.impl;

import com.edee.foundationsforfaith.dtos.ProjectUpdateCreationDto;
import com.edee.foundationsforfaith.entities.Location;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.ProjectUpdate;
import com.edee.foundationsforfaith.entities.actions.CompleteProject;
import com.edee.foundationsforfaith.entities.actions.PauseProject;
import com.edee.foundationsforfaith.entities.actions.ProjectAction;
import com.edee.foundationsforfaith.entities.actions.StartProject;
import com.edee.foundationsforfaith.enums.ProgressStatus;
import com.edee.foundationsforfaith.enums.ProjectType;
import com.edee.foundationsforfaith.enums.ProjectUpdateType;
import com.edee.foundationsforfaith.exceptions.UnableToInsertException;
import com.edee.foundationsforfaith.repositories.ProjectRepository;
import com.edee.foundationsforfaith.repositories.ProjectUpdateRepository;
import com.edee.foundationsforfaith.services.ProjectService;
import com.edee.foundationsforfaith.services.ProjectUpdateService;
import com.edee.foundationsforfaith.utils.EnumUtils;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.edee.foundationsforfaith.enums.ProjectUpdateType.GENERAL_UPDATE;

@Log4j2
@Service
public class ProjectUpdateServiceImpl implements ProjectUpdateService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectUpdateRepository projectUpdateRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public ProjectUpdate createProjectUpdate(ProjectUpdateCreationDto projectUpdateCreationDto){
        Optional<Project> project = projectRepository.findProjectByProjectName(projectUpdateCreationDto.getProjectName());

        if(project.isPresent()){

            ProjectUpdate projectUpdate = new ProjectUpdate();
            projectUpdate.setProjectUpdateType(
                    (projectUpdateCreationDto.getProjectUpdateType() == null) || !(EnumUtils.isEnumValue(projectUpdateCreationDto.getProjectUpdateType(), ProjectUpdateType.class))
                    ? GENERAL_UPDATE
                    : Enum.valueOf(ProjectUpdateType.class, projectUpdateCreationDto.getProjectUpdateType()));
            projectUpdate.setDescription(Jsoup.clean(projectUpdateCreationDto.getDescription(), Safelist.basic()));
            projectUpdate.setEmailAllStones(projectUpdateCreationDto.getEmailAllStones());
            ProjectUpdate savedProjectUpdate = projectUpdateRepository.insert(projectUpdate);

            mongoTemplate.update(Project.class)
                    .matching(Criteria.where("project_name").is(projectUpdateCreationDto.getProjectName()))
                    .apply(new Update().push("project_update_ids").value(savedProjectUpdate))
                    .first();

            return savedProjectUpdate;

        }else{
            throw new UnableToInsertException("Cannot add project update as project with"+projectUpdateCreationDto.getProjectName()+"does  not exist", HttpStatus.NOT_FOUND);
        }
    }

    public void handleAction(ProjectAction action, Project project) {
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
