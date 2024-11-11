package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.Location;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends MongoRepository<Location, ObjectId> {
}
