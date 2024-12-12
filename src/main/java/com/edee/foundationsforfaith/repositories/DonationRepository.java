package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.Donation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DonationRepository extends MongoRepository<Donation, ObjectId> {
    @Query("{'projectId': ?2, 'donation_creation_date': { $gte: ?0, $lte: ?1 }}")
    List<Donation> findAllBetweenDates(LocalDateTime startDate, LocalDateTime endDate, String projectId);

}
