package com.edee.foundationsforfaith.dtos;

import com.edee.foundationsforfaith.enums.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectCreationDto {

    private String country;
    private String area;
    private String projectName;
    private String projectDescription;
    private String projectType;
    private String projectStatus;
    private Integer amountOfFundingRequired;
    private Integer buildingAgeInYears;
    private Boolean previouslyCompletedProject;
    private Boolean siteAcquired;
}
