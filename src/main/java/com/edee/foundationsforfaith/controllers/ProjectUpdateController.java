package com.edee.foundationsforfaith.controllers;

import com.edee.foundationsforfaith.dtos.ProjectUpdateCreationDto;
import com.edee.foundationsforfaith.entities.ProjectUpdate;
import com.edee.foundationsforfaith.exceptions.UnableToInsertException;
import com.edee.foundationsforfaith.services.ProjectUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProjectUpdateController {

    @Autowired
    ProjectUpdateService projectUpdateService;

    @PostMapping("/create/projectUpdate")
    public ResponseEntity<?> addNewProjectUpdate (@RequestBody ProjectUpdateCreationDto projectUpdateCreationDto){
        try{
            return new ResponseEntity<ProjectUpdate>(projectUpdateService.createProjectUpdate(projectUpdateCreationDto), HttpStatus.CREATED);
        } catch (UnableToInsertException e){
            return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
