package com.edee.foundationsforfaith.entities;

import com.edee.foundationsforfaith.enums.ProjectType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Data
@Document(collection = "projects")
public class Renovation extends Project{

    private Integer buildingAgeInYears;

    private Boolean previouslyCompletedProject;

    private Boolean expensiveRenovation;

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
    public Renovation(){
        this.buildingAgeInYears = null;
        this.previouslyCompletedProject = null;
        this.expensiveRenovation = false;
    }

}
