package com.edee.foundationsforfaith.services.impl;

import com.edee.foundationsforfaith.dtos.ProjectUpdateCreationDto;
import com.edee.foundationsforfaith.entities.Location;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.ProjectUpdate;
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
    private ProjectService projectService;
    @Autowired
    private MongoTemplate mongoTemplate;

    public ProjectUpdate createProjectUpdate(ProjectUpdateCreationDto projectUpdateCreationDto){
        String safeProjectName = (Jsoup.clean(projectUpdateCreationDto.getProjectName(), Safelist.basic()));
        Optional<Project> project = projectService.getProjectByProjectName(safeProjectName);

        if(project.isPresent()){
            ProjectUpdate projectUpdate = new ProjectUpdate();
            projectUpdate.setProjectUpdateType(
                    (projectUpdateCreationDto.getProjectUpdateType() == null) || !(EnumUtils.isEnumValue(projectUpdateCreationDto.getProjectUpdateType(), ProjectUpdateType.class))
                    ? GENERAL_UPDATE
                    : Enum.valueOf(ProjectUpdateType.class, projectUpdateCreationDto.getProjectUpdateType()));
            projectUpdate.setDescription(Jsoup.clean(projectUpdateCreationDto.getDescription(), Safelist.basic()));
            projectUpdate.setEmailAllStones(projectUpdateCreationDto.getEmailAllStones());
            ProjectUpdate savedProjectUpdate = projectUpdateRepository.insert(projectUpdate);

            mongoTemplate.update(Location.class)
                    .matching(Criteria.where("project_name").is(safeProjectName))
                    .apply(new Update().push("project_update_ids").value(savedProjectUpdate))
                    .first();

            return projectUpdate;
        }else{
            throw new UnableToInsertException("Cannot create project update as project with"+projectUpdateCreationDto.getProjectName()+"does  not exist", HttpStatus.NOT_FOUND);
        }
    }

}
