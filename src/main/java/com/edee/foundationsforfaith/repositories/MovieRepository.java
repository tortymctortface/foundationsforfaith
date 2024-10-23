package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.Movie;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, ObjectId> {
}
