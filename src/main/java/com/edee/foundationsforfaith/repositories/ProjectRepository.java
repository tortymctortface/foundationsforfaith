package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends MongoRepository<Project, ObjectId> {

    Optional<Project> findProjectByProjectName(String projectName);

}
