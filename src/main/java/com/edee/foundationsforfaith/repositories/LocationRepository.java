package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.Location;
import com.edee.foundationsforfaith.entities.Project;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends MongoRepository<Location, ObjectId> {

    Optional<List<Location>> findLocationsByCountry(String country);

    Optional<Location> findLocationByCountryAndArea(String country,String area);
}
