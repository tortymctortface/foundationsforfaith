package com.edee.foundationsforfaith.entities;

import com.edee.foundationsforfaith.enums.ProjectType;
import com.edee.foundationsforfaith.enums.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "projects")
@Data
@NoArgsConstructor
public class Project {

    @Id
    @Field("project_id")
    private ObjectId projectId;
    @Field("project_name")
    @Indexed(unique = true)
    private String projectName;

    @Field("project_description")
    private String projectDescription;

    @Field("project_created_date")
    private LocalDate projectCreatedDate;

    @Field("project_type")
    private ProjectType projectType;

    @Field("project_status")
    private ProgressStatus projectStatus;

    @Field("amount_of_funding_required")
    private Integer amountOfFundingRequired;

    @Field("fully_funded")
    private boolean fullyFunded;

    @Field("project_build_start_date")
    private LocalDate projectBuildStartDate;

    @Field("completed")
    private boolean completed;

    @Field("stone_ids")
    @DocumentReference(lazy = true)
    private List<String> stoneIds;

    @Field("project_update_ids")
    @DocumentReference(lazy = true)
    private List<ProjectUpdate> projectUpdateIds;

    @Field("donation_ids")
    private List<String> donationIds;

    public Project (String name, ProjectType type, Integer fundingRequired){
        this(name, type, fundingRequired, LocalDate.now());
    }
    public Project (ObjectId id, String name, ProjectType type, Integer fundingRequired){
        this.projectId = id;
        this.projectName = name;
        this.projectType = type;
        this.amountOfFundingRequired = fundingRequired;
    }
    public Project (String name, ProjectType type, Integer fundingRequired, LocalDate projectCreatedDate){
        this.projectName = name;
        this.projectCreatedDate = projectCreatedDate;
        this.projectType = type;
        this.amountOfFundingRequired = fundingRequired;
        this.fullyFunded = false;
        this.projectStatus = ProgressStatus.NEW_PROJECT;
        this.completed = false;
    }

}
