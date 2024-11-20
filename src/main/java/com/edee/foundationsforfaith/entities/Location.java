package com.edee.foundationsforfaith.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(name = "unique_country_area", def = "{'country': 1, 'area': 1}", unique = true)
public class Location {

    @Id
    @Field("location_id")
    private ObjectId locationId;

    @Field("country")
    private String country;

    @Field("area")
    private String area;

    @Field("project_ids")
    @DocumentReference
    private List<Project> projectIds;

}
