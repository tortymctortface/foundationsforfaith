package com.edee.foundationsforfaith.utils;

import com.edee.foundationsforfaith.dtos.ProjectStatsDto;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.Stone;
import com.edee.foundationsforfaith.entities.Donation;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class StreamUtils {

    public static ProjectStatsDto analyzeProjects(List<Project> projects) {
        long totalStones = projects.stream()
                .flatMap(p -> p.getStones().stream())
                .count();

        List<String> stoneTypes = projects.stream()
                .flatMap(p -> p.getStones().stream())
                .map(Stone::getType)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        BigDecimal totalDonations = projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        // Return DTO
        ProjectStatsDto dto = new ProjectStatsDto();
        dto.setTotalStones(totalStones);
        dto.setStoneTypes(stoneTypes);
        dto.setTotalDonations(totalDonations);

        return dto;
    }

}
