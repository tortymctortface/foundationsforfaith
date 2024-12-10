package com.edee.foundationsforfaith.entities;

import com.edee.foundationsforfaith.enums.ProjectType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "projects")
public class Renovation extends Project{

    private Integer buildingAgeInYears;

    private Boolean previouslyCompletedProject;

    public Renovation(String name, ProjectType projectType, Integer fundingRequired, Integer buildingAgeInYears, Boolean previouslyCompletedProject){
        super(name, projectType, fundingRequired);
        this.buildingAgeInYears = buildingAgeInYears;
        this.previouslyCompletedProject = previouslyCompletedProject;
    }

}
