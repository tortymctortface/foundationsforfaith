package com.edee.foundationsforfaith.services.impl;

import com.edee.foundationsforfaith.dtos.ProjectCreationDto;
import com.edee.foundationsforfaith.entities.Location;
import com.edee.foundationsforfaith.entities.NewBuild;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.repositories.ProjectRepository;
import com.edee.foundationsforfaith.services.LocationService;
import com.edee.foundationsforfaith.services.ProjectService;
import com.edee.foundationsforfaith.utils.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public final class NewBuildServiceImpl extends ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Project createProject(ProjectCreationDto projectCreationDto) {
        Project project = new NewBuild(projectCreationDto.getProjectName(),
                EnumUtils.getProjectType(projectCreationDto.getProjectType()),
                projectCreationDto.getAmountOfFundingRequired() == null ? 0 : projectCreationDto.getAmountOfFundingRequired(),
                projectCreationDto.getSiteAcquired());
        Project savedProject = projectRepository.insert(project);

        Location location = locationService.findOrCreateLocation(projectCreationDto.getCountry(), projectCreationDto.getArea());

        mongoTemplate.update(Location.class)
                .matching(Criteria.where("country").is(location.getCountry()).and("area").is(location.getArea()))
                .apply(new Update().push("project_ids").value(savedProject))
                .first();

        return project;
    }
}
