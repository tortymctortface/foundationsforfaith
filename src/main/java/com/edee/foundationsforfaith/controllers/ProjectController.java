package com.edee.foundationsforfaith.controllers;

import com.edee.foundationsforfaith.dtos.ProjectCreationDto;
import com.edee.foundationsforfaith.entities.NewBuild;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.repositories.ProjectRepository;
import com.edee.foundationsforfaith.services.LocationService;
import com.edee.foundationsforfaith.services.ProjectService;
import com.edee.foundationsforfaith.services.ProjectUpdateService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService newBuildServiceImpl;
    private final ProjectService renovationServiceImpl;
    private final ProjectRepository projectRepository;
    @Autowired
    public ProjectController(@Qualifier("newBuildServiceImpl") ProjectService newBuildServiceImpl,
                             @Qualifier("renovationServiceImpl") ProjectService renovationServiceImpl,
                             ProjectRepository projectRepository) {
        this.newBuildServiceImpl = newBuildServiceImpl;
        this.renovationServiceImpl = renovationServiceImpl;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> allProjects(){
        return new ResponseEntity<List<Project>>(projectRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/project/{name}")
    public ResponseEntity<Optional<Project>> projectById(@PathVariable String projectName){
        String safeProjectName = Jsoup.clean(projectName, Safelist.basic());
        return new ResponseEntity<Optional<Project>>(projectRepository.findProjectByProjectName(safeProjectName), HttpStatus.OK);
    }

    @PostMapping("/create/project/newBuild")
    public ResponseEntity<?> createNewBuildProject (@RequestBody ProjectCreationDto projectCreationDto){
        try {
            return new ResponseEntity<Project>(newBuildServiceImpl.createProject(projectCreationDto), HttpStatus.CREATED);
        }catch (DuplicateKeyException duplicateKeyException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Duplicate project name used. Error: " + duplicateKeyException.getMessage());
        }
    }

    @PostMapping("/create/project/renovation")
    public ResponseEntity<?> createRenovationProject (@RequestBody ProjectCreationDto projectCreationDto){
        try{
            return new ResponseEntity<Project>(renovationServiceImpl.createProject(projectCreationDto), HttpStatus.CREATED);
        }catch (DuplicateKeyException duplicateKeyException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Duplicate project name used. Error: " + duplicateKeyException.getMessage());
        }
    }

}
