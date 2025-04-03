package com.edee.foundationsforfaith.dtos;

import com.edee.foundationsforfaith.enums.StoneType;
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
    private StoneType stoneType;
}
