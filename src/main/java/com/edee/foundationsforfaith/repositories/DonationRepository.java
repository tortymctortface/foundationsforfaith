package com.edee.foundationsforfaith.repositories;

import com.edee.foundationsforfaith.entities.Donation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DonationRepository extends MongoRepository<Donation, ObjectId> {
    @Query("SELECT  FROM donations d WHERE d.projectId = projectId AND d.donationCreationDate BETWEEN :startDate AND :endDate")
    List<Donation> findAllBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("endDate") String projectId);

}
