package com.edee.foundationsforfaith.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    @Id
    private ObjectId locationId;

    private String countryCode;

    private String areaName;

    @DocumentReference
    private List<Project> projectIds;

}
