package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.ProjectCreationDto;
import com.edee.foundationsforfaith.entities.Project;

import java.util.Arrays;


public interface ProjectService {
    Project createProject(ProjectCreationDto projectCreationDto);
    Project getDefensiveProjectByProjectName(String projectName);

    default float getAverageRating(float... donations) {
        if (donations == null || donations.length == 0) {
            return 0.0f;
        }
        return calculateAverage(donations);
    }

    private float calculateAverage(float... donations) {
        float total = 0;
        for (float d : donations) {
            total += d;
        }
        return donations.length == 0 ? 0 : total / donations.length;
    }

    static boolean isValidDonationAmount(float amount) {
        return amount >= 1;
    }

}
