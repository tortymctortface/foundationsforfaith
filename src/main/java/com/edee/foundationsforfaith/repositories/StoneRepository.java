package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.Stone;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoneRepository extends MongoRepository<Stone, ObjectId> {
    boolean existsByEmail(String email);
}
