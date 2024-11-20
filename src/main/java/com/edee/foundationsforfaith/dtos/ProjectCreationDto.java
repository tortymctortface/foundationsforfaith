package com.edee.foundationsforfaith.dtos;

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
    private Integer amountOfFundingRequired;
}
