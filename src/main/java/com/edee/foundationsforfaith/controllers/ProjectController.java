package com.edee.foundationsforfaith.controllers;

import com.edee.foundationsforfaith.dtos.NewProjectDto;
import com.edee.foundationsforfaith.entities.Location;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.services.LocationService;
import com.edee.foundationsforfaith.services.ProjectService;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    LocationService locationService;

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> allProjects(){
        return new ResponseEntity<List<Project>>(projectService.getAllProjects(), HttpStatus.OK);
    }

    @GetMapping("/project/{name}")
    public ResponseEntity<Optional<Project>> projectById(@PathVariable String projectName){
        String safeProjectName = Jsoup.clean(projectName, Safelist.basic());
        return new ResponseEntity<Optional<Project>>(projectService.getProjectByProjectName(safeProjectName), HttpStatus.OK);
    }

    @PostMapping("/create/project")
    public ResponseEntity<Project> createNewLocation (NewProjectDto newProjectDto){
        String safeCountry = Jsoup.clean(newProjectDto.getCountry(), Safelist.basic());
        String safeArea = Jsoup.clean(newProjectDto.getArea(), Safelist.basic());
        Location location = locationService.createLocation(safeCountry,safeArea);
        return new ResponseEntity<Project>((projectService.createProject(location.getLocationId(), newProjectDto)), HttpStatus.CREATED);
    }

}
