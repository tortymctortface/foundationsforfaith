package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.StoneCreationDto;
import com.edee.foundationsforfaith.entities.Stone;

import java.util.Optional;

public interface StoneService {
    Stone createStoneToAssociateWithProject(StoneCreationDto stoneCreationDto);
    Optional<Stone> getStoneByEmail(String stoneEmail);
}
