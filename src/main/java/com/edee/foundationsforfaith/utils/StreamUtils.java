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

        List<String> topDonors = projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .filter(d -> d.getDonorName() != null)
                .sorted((d1, d2) -> d2.getAmount().compareTo(d1.getAmount()))
                .limit(5)
                .map(d -> d.getDonorName() + ": $" + d.getAmount())
                .collect(Collectors.toList());

        BigDecimal minDonation = projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .map(Donation::getAmount)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal maxDonation = projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .map(Donation::getAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        Map<String, BigDecimal> totalByDonor = projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .filter(d -> d.getDonorName() != null)
                .collect(Collectors.toMap(
                        Donation::getDonorName,
                        Donation::getAmount,
                        BigDecimal::add
                ));

        Map<String, Integer> donationCountByProject = projects.stream()
                .collect(Collectors.toMap(
                        Project::getName,
                        p -> p.getDonations().size()
                ));

        Map<Boolean, List<Donation>> partitioned = projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .collect(Collectors.partitioningBy(
                        d -> d.getAmount().compareTo(new BigDecimal("500")) >= 0
                ));

        int highDonationCount = partitioned.get(true).size();
        int lowDonationCount = partitioned.get(false).size();

        // Return DTO
        ProjectStatsDto dto = new ProjectStatsDto();
        dto.setTotalStones(totalStones);
        dto.setStoneTypes(stoneTypes);
        dto.setTotalDonations(totalDonations);
        dto.setTopDonors(topDonors);
        dto.setMinDonation(minDonation);
        dto.setMaxDonation(maxDonation);
        dto.setTotalByDonor(totalByDonor);
        dto.setDonationCountByProject(donationCountByProject);
        dto.setHighDonationCount(highDonationCount);
        dto.setLowDonationCount(lowDonationCount);

        return dto;
    }

}
