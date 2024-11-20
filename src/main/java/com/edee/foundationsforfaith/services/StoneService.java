package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.StoneCreationDto;
import com.edee.foundationsforfaith.entities.Stone;

public interface StoneService {
    Stone createStoneToAssociateWithProject(StoneCreationDto stoneCreationDto);
}
