package com.edee.foundationsforfaith.controllers;

import com.edee.foundationsforfaith.dtos.ProjectCreationDto;
import com.edee.foundationsforfaith.dtos.StoneCreationDto;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.Stone;
import com.edee.foundationsforfaith.exceptions.UnableToInsertException;
import com.edee.foundationsforfaith.services.StoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StoneController {

    @Autowired
    StoneService stoneService;

    @PostMapping("/create/stone")
    public ResponseEntity<?> createStone (@RequestBody StoneCreationDto stoneCreationDto){
            return new ResponseEntity<Stone>(stoneService.createStoneToAssociateWithProject(stoneCreationDto), HttpStatus.CREATED);

    }
}
