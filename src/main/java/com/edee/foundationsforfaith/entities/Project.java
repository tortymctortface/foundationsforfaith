package com.edee.foundationsforfaith.entities;

import com.edee.foundationsforfaith.constants.ApplicationConstants;
import com.edee.foundationsforfaith.enums.ProjectType;
import com.edee.foundationsforfaith.enums.ProgressStatus;
import com.edee.foundationsforfaith.utils.CalculationUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

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

    @Field("funding_acquired")
    private Integer fundingAcquired;

    @Field("project_build_start_date")
    private LocalDate projectBuildStartDate;
    @Field("completed")
    private boolean completed;

    @Field("stone_ids")
    private  List<Stone> stoneIds;

    @Field("project_update_ids")
    private List<ProjectUpdate> projectUpdateIds;

    @Field("donation_ids")
    private  List<Donation>  donationIds;

    public Project (String name, ProjectType type, String status, Integer fundingRequired){
        this(name, type, status,fundingRequired, LocalDate.now());
    }
    public Project (ObjectId id, String name, ProjectType type, Integer fundingRequired){
        this.projectId = id;
        this.projectName = name;
        this.projectType = type;
        this.amountOfFundingRequired = fundingRequired;
    }
    public Project (String name, ProjectType type, String status, Integer fundingRequired, LocalDate projectCreatedDate){
        this.projectName = name;
        this.projectCreatedDate = projectCreatedDate;
        this.projectType = type;
        this.amountOfFundingRequired = fundingRequired;
        this.projectStatus = switch (status) {
            case "FUND_RAISING" -> ProgressStatus.FUND_RAISING;
            case "FUNDING_ACHIEVED" -> ProgressStatus.FUNDING_ACHIEVED;
            case "SEARCHING_FOR_SITE" -> ProgressStatus.SEARCHING_FOR_SITE;
            case "SITE_PURCHASED" -> ProgressStatus.SITE_PURCHASED;
            case "BUILD_START_SCHEDULED" -> ProgressStatus.BUILD_START_SCHEDULED;
            case "BUILD_IN_PROGRESS" -> ProgressStatus.BUILD_IN_PROGRESS;
            case "EXTERIOR_BUILD_COMPLETE" -> ProgressStatus.EXTERIOR_BUILD_COMPLETE;
            case "FURNISHING_IN_PROGRESS" -> ProgressStatus.FURNISHING_IN_PROGRESS;
            case "BUILD_COMPLETE" -> ProgressStatus.BUILD_COMPLETE;
            default -> ProgressStatus.NEW_PROJECT;
        };
        this.completed = false;
    }

    public boolean expensiveProject(Integer value){
        Predicate<Integer> isLarge = num -> num >= ApplicationConstants.requiresSubstantialFunding;
        return isLarge.test(value);
    }

    public boolean expensiveNewBuild(Integer value){
        Predicate<Integer> isLarge = CalculationUtils::isExpensive;
        return isLarge.test(value);
    }

}
