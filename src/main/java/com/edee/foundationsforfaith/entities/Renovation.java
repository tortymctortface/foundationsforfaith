package com.edee.foundationsforfaith.entities;

import com.edee.foundationsforfaith.enums.ProjectType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "projects")
public class Renovation extends Project{

    private final Integer buildingAgeInYears;

    private final Boolean previouslyCompletedProject;

    private final Boolean expensiveRenovation;

    public Renovation(String name, ProjectType projectType, String progressStatus, Integer fundingRequired, Integer buildingAgeInYears, Boolean previouslyCompletedProject){
        super(name, projectType, progressStatus, fundingRequired);
        this.buildingAgeInYears = buildingAgeInYears;
        this.previouslyCompletedProject = previouslyCompletedProject;
        this.expensiveRenovation = super.expensiveProject(fundingRequired);
    }

    public Renovation(String name, ProjectType projectType, String progressStatus, Integer fundingRequired, Integer buildingAgeInYears, Boolean previouslyCompletedProject, LocalDate date){
        super(name, projectType, progressStatus, fundingRequired, date);
        this.buildingAgeInYears = buildingAgeInYears;
        this.previouslyCompletedProject = previouslyCompletedProject;
        this.expensiveRenovation = super.expensiveProject(fundingRequired);
    }

}
