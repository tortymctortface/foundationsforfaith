package com.edee.foundationsforfaith.entities;

import com.edee.foundationsforfaith.enums.ProjectUpdateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "projectUpdates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdates {

    @Id
    private ObjectId projectUpdatesId;

    private ProjectUpdateType projectUpdateType;

    private String description;


}
