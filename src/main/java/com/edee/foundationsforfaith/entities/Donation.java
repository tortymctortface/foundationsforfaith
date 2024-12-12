package com.edee.foundationsforfaith.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "donations")
@Data
@NoArgsConstructor
public class Donation {

    @Id
    @Field("donation_id")
    private ObjectId donationId;

    @Field("donation_message")
    private String donorMessage;

    @Field("donation_amount")
    private Integer donationAmount;

    @Field("donation_creation_date")
    private LocalDate donationCreationDate = LocalDate.now();

    @Field("project_id")
    private String projectId;

    @Field("stone_id")
    private String stoneId;

}
