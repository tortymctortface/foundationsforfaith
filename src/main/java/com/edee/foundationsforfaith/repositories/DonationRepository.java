package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.Donation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DonationRepository extends MongoRepository<Donation, ObjectId> {
}
