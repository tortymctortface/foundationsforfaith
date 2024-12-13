package com.edee.foundationsforfaith.entities;

import com.edee.foundationsforfaith.enums.ProgressStatus;
import com.edee.foundationsforfaith.enums.ProjectType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
@Data
@Document(collection = "projects")
public class NewBuild extends Project{

    private Boolean siteAcquired;
    private Boolean expensiveBuild;

    public NewBuild(String name, ProjectType projectType, Integer fundingRequired, Boolean siteAcquired){
        super(name, projectType, String.valueOf(ProgressStatus.NEW_PROJECT), fundingRequired);
        this.siteAcquired = siteAcquired;
        this.expensiveBuild = super.expensiveNewBuild(fundingRequired);
    }

    public NewBuild(String name, ProjectType projectType, Integer fundingRequired, Boolean siteAcquired, LocalDate date){
        super(name, projectType,String.valueOf(ProgressStatus.NEW_PROJECT), fundingRequired, date);
        this.siteAcquired = siteAcquired;
        this.expensiveBuild = super.expensiveNewBuild(fundingRequired);
    }

    public NewBuild(){
        this.siteAcquired = null;
        this.expensiveBuild =  false;
    }


}
