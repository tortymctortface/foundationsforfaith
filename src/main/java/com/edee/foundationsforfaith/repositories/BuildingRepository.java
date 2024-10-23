package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.Building;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BuildingRepository extends MongoRepository<Building, ObjectId> {
}
