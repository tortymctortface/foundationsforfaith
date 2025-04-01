package com.edee.foundationsforfaith.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectActionDto {
    private String type; // e.g. "START", "PAUSE", "COMPLETE"
    private String projectName;

}