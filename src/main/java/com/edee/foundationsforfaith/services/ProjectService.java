package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.ProjectCreationDto;
import com.edee.foundationsforfaith.entities.Project;

import java.util.Arrays;


public interface ProjectService {
    Project createProject(ProjectCreationDto projectCreationDto);
    Project getDefensiveProjectByProjectName(String projectName);

    default double getAverageRating(int... donations) {
        if (donations == null || donations.length == 0) {
            return 0.0;
        }
        return calculateAverage(donations);
    }

    private double calculateAverage(int... donations) {
        int total = Arrays.stream(donations).sum();
        int length = donations.length;
        return total/length;
    }

    static boolean isValidDonationAmount(int amount) {
        return amount >= 1;
    }

}
