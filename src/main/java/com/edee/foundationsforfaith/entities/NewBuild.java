package com.edee.foundationsforfaith.entities;

import com.edee.foundationsforfaith.enums.ProgressStatus;
import com.edee.foundationsforfaith.enums.ProjectType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "projects")
public class NewBuild extends Project{

    private final Boolean siteAcquired;
    private final Boolean expensiveBuild;

    public NewBuild(String name, ProjectType projectType, Integer fundingRequired, Boolean siteAcquired){
        super(name, projectType, String.valueOf(ProgressStatus.NEW_PROJECT), fundingRequired);
        this.siteAcquired = siteAcquired;
        this.expensiveBuild = super.expensiveProject(fundingRequired);
    }

    public NewBuild(String name, ProjectType projectType, Integer fundingRequired, Boolean siteAcquired, LocalDate date){
        super(name, projectType,String.valueOf(ProgressStatus.NEW_PROJECT), fundingRequired, date);
        this.siteAcquired = siteAcquired;
        this.expensiveBuild = super.expensiveProject(fundingRequired);
    }

    public NewBuild(){
        this.siteAcquired = null;
        this.expensiveBuild =  false;
    }


}
