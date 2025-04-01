package com.edee.foundationsforfaith.utils;

import com.edee.foundationsforfaith.dtos.ProjectDonationTotalDto;
import com.edee.foundationsforfaith.dtos.ProjectStatsDto;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.Stone;
import com.edee.foundationsforfaith.entities.Donation;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Log4j2
public class StatisticUtils {

    public static ProjectStatsDto analyzeProjects(List<Project> projects) {
        Instant start = Instant.now();

        ProjectStatsDto dto = new ProjectStatsDto();

        computeStoneStats(projects, dto);
        computeDonationSummary(projects, dto);
        computeDonorBreakdown(projects, dto);
        computeProjectStats(projects, dto);

        Instant end = Instant.now();
        log.info("analyzeProjects took: {} ms", Duration.between(start, end).toMillis());

        return dto;
    }

    public static ProjectStatsDto analyzeProjectsConcurrent(List<Project> projects) throws InterruptedException, ExecutionException {
        Instant start = Instant.now();
        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {
            Future<ProjectStatsDto> stoneFuture = executor.submit(() -> {
                ProjectStatsDto dto = new ProjectStatsDto();
                computeStoneStats(projects, dto);
                return dto;
            });

            Future<ProjectStatsDto> donationFuture = executor.submit(() -> {
                ProjectStatsDto dto = new ProjectStatsDto();
                computeDonationSummary(projects, dto);
                return dto;
            });

            Future<ProjectStatsDto> donorFuture = executor.submit(() -> {
                ProjectStatsDto dto = new ProjectStatsDto();
                computeDonorBreakdown(projects, dto);
                return dto;
            });

            Future<ProjectStatsDto> projectFuture = executor.submit(() -> {
                ProjectStatsDto dto = new ProjectStatsDto();
                computeProjectStats(projects, dto);
                return dto;
            });

            ProjectStatsDto finalDto = new ProjectStatsDto();
            mergeDtos(finalDto, stoneFuture.get(), donationFuture.get(), donorFuture.get(), projectFuture.get());

            Instant end = Instant.now();
            log.info("analyzeProjectsConcurrent took: {} ms", Duration.between(start, end).toMillis());

            return finalDto;
        } finally {
            executor.shutdown();
        }
    }

    private static void computeStoneStats(List<Project> projects, ProjectStatsDto dto) {
        dto.setStoneCount(projects.stream()
                .flatMap(p -> p.getStones().stream())
                .count());

        dto.setStoneTypes(projects.stream()
                .flatMap(p -> p.getStones().stream())
                .map(Stone::getType)
                .distinct()
                .sorted()
                .collect(Collectors.toList()));
    }

    private static void computeDonationSummary(List<Project> projects, ProjectStatsDto dto) {
        dto.setTotalDonationAmount(projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        dto.setMinDonationAmount(projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .map(Donation::getAmount)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO));

        dto.setMaxDonationAmount(projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .map(Donation::getAmount)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO));

        Map<Boolean, List<Donation>> partitioned = projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .collect(Collectors.partitioningBy(d -> d.getAmount().compareTo(new BigDecimal("500")) >= 0));

        dto.setHighDonationCount(partitioned.getOrDefault(true, List.of()).size());
        dto.setLowDonationCount(partitioned.getOrDefault(false, List.of()).size());
    }

    private static void computeDonorBreakdown(List<Project> projects, ProjectStatsDto dto) {
        dto.setTopDonorList(projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .filter(d -> d.getDonorName() != null)
                .sorted((d1, d2) -> d2.getAmount().compareTo(d1.getAmount()))
                .limit(5)
                .map(d -> d.getDonorName() + ": $" + d.getAmount())
                .collect(Collectors.toList()));

        dto.setDonationTotalsByDonor(projects.stream()
                .flatMap(p -> p.getDonations().stream())
                .filter(d -> d.getDonorName() != null)
                .collect(Collectors.toMap(
                        Donation::getDonorName,
                        Donation::getAmount,
                        BigDecimal::add
                )));
    }

    private static void computeProjectStats(List<Project> projects, ProjectStatsDto dto) {
        dto.setDonationCountPerProject(projects.stream()
                .collect(Collectors.toMap(
                        Project::getName,
                        p -> p.getDonations().size()
                )));

        dto.setProjectDurationMap(projects.stream()
                .filter(p -> p.getStartDate() != null && p.getEndDate() != null)
                .collect(Collectors.toMap(
                        Project::getName,
                        p -> {
                            Period duration = Period.between(p.getStartDate(), p.getEndDate());
                            return String.format("%d months, %d days", duration.getMonths(), duration.getDays());
                        }
                )));
    }

    private static void mergeDtos(ProjectStatsDto target, ProjectStatsDto... sources) {
        for (ProjectStatsDto src : sources) {
            if (!src.getStoneCount().isEmpty()) target.setStoneCount(src.getStoneCount());
            if (src.getStoneTypes() != null) target.setStoneTypes(src.getStoneTypes());
            if (src.getTotalDonationAmount() != null) target.setTotalDonationAmount(src.getTotalDonationAmount());
            if (src.getMinDonationAmount() != null) target.setMinDonationAmount(src.getMinDonationAmount());
            if (src.getMaxDonationAmount() != null) target.setMaxDonationAmount(src.getMaxDonationAmount());
            if (src.getHighDonationCount() != 0) target.setHighDonationCount(src.getHighDonationCount());
            if (src.getLowDonationCount() != 0) target.setLowDonationCount(src.getLowDonationCount());
            if (src.getTopDonorList() != null) target.setTopDonorList(src.getTopDonorList());
            if (src.getDonationTotalsByDonor() != null) target.setDonationTotalsByDonor(src.getDonationTotalsByDonor());
            if (src.getDonationCountPerProject() != null) target.setDonationCountPerProject(src.getDonationCountPerProject());
            if (src.getProjectDurationMap() != null) target.setProjectDurationMap(src.getProjectDurationMap());
        }
    }

    public static List<ProjectDonationTotalDto> sortProjectsByDonation(List<Project> projects) {
        return projects.stream()
                .sorted(Comparator.comparing(project ->
                        project.getDonations().stream()
                                .map(Donation::getDonationAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ).reversed())
                .map(p -> new ProjectDonationTotalDto(
                        p.getProjectName(),
                        p.getDonationIds().stream()
                                .map(Donation::getDonationAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                ))
                .collect(Collectors.toList());
    }
}
