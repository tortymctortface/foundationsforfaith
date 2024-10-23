package com.edee.foundationsforfaith.entities;

import com.edee.foundationsforfaith.enums.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "buildings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Building {

    @Id
    private ObjectId buildingId;

    private String buildingName;

    private String buildingType;

    private ProgressStatus buildingStatus;

    private Integer percentageOfFundingAchieved;

    private Integer percentageOfBuildComplete;

    @DocumentReference
    private List<Stone> stoneIds;

}
