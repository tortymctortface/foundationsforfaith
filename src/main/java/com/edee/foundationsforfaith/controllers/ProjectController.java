package com.edee.foundationsforfaith.controllers;

import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.services.ProjectService;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> allProjects(){
        return new ResponseEntity<List<Project>>(projectService.getAllProjects(), HttpStatus.OK);
    }

    @GetMapping("/project/{name}")
    public ResponseEntity<Optional<Project>> projectById(@PathVariable String projectName){
        String safeProjectName = Jsoup.clean(projectName, Safelist.basic());
        return new ResponseEntity<Optional<Project>>(projectService.getProjectByProjectName(safeProjectName), HttpStatus.OK);
    }

}
