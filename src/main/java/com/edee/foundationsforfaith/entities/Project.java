package com.edee.foundationsforfaith.entities;

import com.edee.foundationsforfaith.enums.BuildingType;
import com.edee.foundationsforfaith.enums.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    private ObjectId projectId;

    private String projectName;

    private String projectDescription;

    private LocalDate projectCreatedDate;

    private BuildingType buildingType;

    private ProgressStatus projectStatus;

    private Integer amountOfFundingRequired;

    private boolean fullyFunded;

    private LocalDate projectBuildStartDate;

    private boolean completed;

    @DocumentReference
    private List<Stone> stoneIds;

    @DocumentReference
    private List<ProjectUpdates> projectUpdates;

}
