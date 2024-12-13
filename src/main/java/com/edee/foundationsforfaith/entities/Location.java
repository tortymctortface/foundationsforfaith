package com.edee.foundationsforfaith.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "locations")
@CompoundIndex(name = "unique_country_area", def = "{'country': 1, 'area': 1}", unique = true)
public final class Location {

    @Id
    @Field("location_id")
    private final ObjectId locationId;

    @Field("country")
    private final String country;

    @Field("area")
    private final String area;

    @Field("project_ids")
    private final List<String> projectIds;

    public Location(ObjectId locationId, String country, String area, List<String> projectIds) {
        if (country == null || area == null) {
            throw new IllegalArgumentException("Location parameters cannot be null");
        }
        this.locationId = locationId;
        this.country = country;
        this.area = area;
        this.projectIds = projectIds;
    }

    public ObjectId getLocationId() {
        return locationId;
    }

    public String getCountry() {
        return country;
    }

    public String  getArea() {
        return area;
    }

    public List<String> getProjectIds() {
        return projectIds;
    }

    public Location withId(ObjectId locationId) {
        if (locationId == null) {
            throw new IllegalArgumentException("dbId cannot be null");
        }
        return new Location(locationId, this.country, this.area, this.projectIds);
    }

    @Override
    public String toString() {
        return "Location{locationId=" + locationId + ", country='" + country + "', area='" + area + "', projectIds=" + projectIds + "}";
    }



}
