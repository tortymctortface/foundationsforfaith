package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.Stone;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StoneRepository extends MongoRepository<Stone, ObjectId> {
    boolean existsByEmail(String email);

    Optional<Stone> findStoneByEmail(String stoneEmail);
}
