package com.edee.foundationsforfaith.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "stones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stone {

    @Id
    @Field("description")
    private ObjectId stoneId;

    @Field("email")
    @Indexed(unique = true)
    private String email;

    @Field("donor_name")
    private String donorName;

    @Field("send_updates_to_donor")
    private Boolean sendUpdatesToDonor;

    @Field("donation_ids")
    @DocumentReference
    private List<Donation> donationIds;

}
