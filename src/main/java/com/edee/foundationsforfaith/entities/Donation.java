package com.edee.foundationsforfaith.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Float donationAmount;

    @Field("large_donation")
    private Boolean largeDonation;

    @Field("donation_creation_date")
    private LocalDate donationCreationDate = LocalDate.now();

    @JsonIgnore
    private Project projectId;

    @JsonIgnore
    private Stone stoneId;

}
