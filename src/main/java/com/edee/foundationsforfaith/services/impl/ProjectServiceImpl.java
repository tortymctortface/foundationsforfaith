//package com.edee.foundationsforfaith.services.impl;
//
//import com.edee.foundationsforfaith.dtos.ProjectCreationDto;
//import com.edee.foundationsforfaith.entities.Project;
//import com.edee.foundationsforfaith.entities.Location;
//import com.edee.foundationsforfaith.enums.ProjectType;
//import com.edee.foundationsforfaith.enums.ProgressStatus;
//import com.edee.foundationsforfaith.repositories.ProjectRepository;
//import com.edee.foundationsforfaith.services.LocationService;
//import com.edee.foundationsforfaith.services.ProjectService;
//import com.edee.foundationsforfaith.utils.EnumUtils;
//import org.jsoup.Jsoup;
//import org.jsoup.safety.Safelist;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public abstract class ProjectServiceImpl implements ProjectService {
//
//    @Autowired
//    private ProjectRepository projectRepository;
//    @Autowired
//    private LocationService locationService;
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Override
//    public List<Project> getAllProjects(){
//        return projectRepository.findAll();
//    }
//
//    @Override
//    public Optional<Project> getProjectByProjectName(String projectName) {
//        return Optional.empty();
//    }
//
//
//
//}
