package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.ProjectUpdate;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectUpdateRepository extends MongoRepository<ProjectUpdate, ObjectId> {
}
