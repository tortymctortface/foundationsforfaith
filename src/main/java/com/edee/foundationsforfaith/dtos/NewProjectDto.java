package com.edee.foundationsforfaith.dtos;

import com.edee.foundationsforfaith.enums.BuildingType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewProjectDto {

    private String country;
    private String area;
    private String projectName;
    private String projectDescription;
    private BuildingType buildingType;
    private Integer amountOfFundingRequired;
}
