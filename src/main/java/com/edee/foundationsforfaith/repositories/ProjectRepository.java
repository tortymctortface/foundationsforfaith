package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, ObjectId> {
}
