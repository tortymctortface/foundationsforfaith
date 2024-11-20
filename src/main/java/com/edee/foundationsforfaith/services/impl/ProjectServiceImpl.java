package com.edee.foundationsforfaith.services.impl;

import com.edee.foundationsforfaith.dtos.ProjectCreationDto;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.Location;
import com.edee.foundationsforfaith.enums.ProjectType;
import com.edee.foundationsforfaith.enums.ProgressStatus;
import com.edee.foundationsforfaith.repositories.ProjectRepository;
import com.edee.foundationsforfaith.services.LocationService;
import com.edee.foundationsforfaith.services.ProjectService;
import com.edee.foundationsforfaith.utils.EnumUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private LocationService locationService;
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectByProjectName(String projectName){
        return projectRepository.findProjectByProjectName(projectName);
    }

    public Project createProject(ProjectCreationDto projectCreationDto){
        Project project = new Project();
        project.setProjectName(Jsoup.clean(projectCreationDto.getProjectName(), Safelist.basic()));
        project.setProjectDescription(Jsoup.clean(projectCreationDto.getProjectDescription(), Safelist.basic()));
        project.setProjectType(
                (projectCreationDto.getProjectType() == null) || !(EnumUtils.isEnumValue(projectCreationDto.getProjectType(), ProjectType.class))
                        ? ProjectType.UNKNOWN
                        : Enum.valueOf(ProjectType.class, projectCreationDto.getProjectType()));
        project.setProjectCreatedDate(LocalDate.now());
        project.setAmountOfFundingRequired(projectCreationDto.getAmountOfFundingRequired() == null? 0 : projectCreationDto.getAmountOfFundingRequired() );
        project.setProjectStatus(ProgressStatus.NEW_PROJECT);
        project.setCompleted(false);
        project.setFullyFunded(false);
        Project savedProject = projectRepository.insert(project);

        Location location = locationService.findOrCreateLocation(projectCreationDto.getCountry(), projectCreationDto.getArea());

        mongoTemplate.update(Location.class)
                .matching(Criteria.where("country").is(location.getCountry()).and("area").is(location.getArea()))
                .apply(new Update().push("project_ids").value(savedProject))
                .first();

        return project;
    }

}
