package com.edee.foundationsforfaith.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "donations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Donation {

    @Id
    @Field("donation_id")
    private ObjectId donationId;

    @Field("donation_message")
    private String donorMessage;

    @Field("donation_amount")
    private String donationAmount;

    private String projectId;

    private String stoneId;

}
