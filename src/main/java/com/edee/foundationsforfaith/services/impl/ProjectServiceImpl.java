package com.edee.foundationsforfaith.services.impl;

import com.edee.foundationsforfaith.dtos.NewProjectDto;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.Location;
import com.edee.foundationsforfaith.enums.BuildingType;
import com.edee.foundationsforfaith.enums.ProgressStatus;
import com.edee.foundationsforfaith.repositories.ProjectRepository;
import com.edee.foundationsforfaith.services.ProjectService;
import org.bson.types.ObjectId;
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
    private MongoTemplate mongoTemplate;

    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectByProjectName(String projectName){
        return projectRepository.findProjectByProjectName(projectName);
    }

    public Project createProject(NewProjectDto newProjectDto){
        Project project = new Project();
        project.setProjectName(newProjectDto.getProjectName());
        project.setProjectDescription(newProjectDto.getProjectDescription());
        project.setBuildingType(newProjectDto.getBuildingType() == null ? BuildingType.UNKNOWN : newProjectDto.getBuildingType());
        project.setProjectCreatedDate(LocalDate.now());
        project.setAmountOfFundingRequired(newProjectDto.getAmountOfFundingRequired() == null? 0 : newProjectDto.getAmountOfFundingRequired() );
        project.setProjectStatus(ProgressStatus.NEW_PROJECT);
        project.setCompleted(false);
        project.setFullyFunded(false);
        projectRepository.insert(project);

        mongoTemplate.update(Location.class)
                .matching(Criteria.where("area").is(newProjectDto.getArea()).and("country").is(newProjectDto.getCountry()))
                .apply(new Update().push("projectIds").value(project));

        return project;
    }
}
