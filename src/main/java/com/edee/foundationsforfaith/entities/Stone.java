package com.edee.foundationsforfaith.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stone {

    @Id
    private ObjectId stoneId;

    private String donorName;

    private String donorMessage;

    private String donationAmount;

}
