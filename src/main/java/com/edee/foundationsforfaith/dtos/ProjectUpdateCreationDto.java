package com.edee.foundationsforfaith.dtos;

import com.edee.foundationsforfaith.enums.ProjectUpdateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectUpdateCreationDto {
    private String projectName;
    private String projectUpdateType;
    private String description;
    private Boolean emailAllStones;
}
