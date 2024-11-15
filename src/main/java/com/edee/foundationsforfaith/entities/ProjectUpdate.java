package com.edee.foundationsforfaith.entities;

import com.edee.foundationsforfaith.enums.ProjectUpdateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "project_updates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdate {

    @Id
    @Field("project_update_id")
    private ObjectId projectUpdateId;

    @Field("project_update_type")
    private ProjectUpdateType projectUpdateType;

    @Field("description")
    private String description;

    @Field("email_all_stones")
    private Boolean emailAllStones;

}
