package com.edee.foundationsforfaith.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoneCreationDto {
    private String projectName;
    private String emailAddress;
    private String donorName;
    private Boolean sendUpdatesToDonor;
}
