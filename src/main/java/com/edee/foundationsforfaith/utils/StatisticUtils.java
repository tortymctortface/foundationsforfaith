package com.edee.foundationsforfaith.utils;

import com.edee.foundationsforfaith.dtos.DonationDto;
import com.edee.foundationsforfaith.dtos.ProjectDonationTotalDto;
import com.edee.foundationsforfaith.dtos.ProjectStatsDto;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.Stone;
import lombok.extern.log4j.Log4j2;

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
        dto.setNumberOfDonors(projects.stream()
                .mapToLong(p -> p.getStoneIds().size())
                .sum());

        dto.setDonorTypes(projects.stream()
                .flatMap(p -> p.getStoneIds().stream())
                .map(Stone::getStoneType)
                .distinct()
                .sorted()
                .collect(Collectors.toList()));
    }

    private static void computeDonationSummary(List<Project> projects, ProjectStatsDto dto) {
        Optional<Float> sum = projects.stream()
                .filter(p -> p.getDonationIds() != null)
                .flatMap(p -> p.getDonationIds().stream())
                .map(DonationDto::getDonationAmount)
                .reduce(Float::sum);

        dto.setTotalDonations(sum.orElse(null)); // cleaner than isEmpty()/get()

        dto.setMinDonation(projects.stream()
                .filter(p -> p.getDonationIds() != null)
                .flatMap(p -> p.getDonationIds().stream())
                .map(DonationDto::getDonationAmount)
                .min(Float::compareTo)
                .orElse(null));

        dto.setMaxDonation(projects.stream()
                .filter(p -> p.getDonationIds() != null)
                .flatMap(p -> p.getDonationIds().stream())
                .map(DonationDto::getDonationAmount)
                .min(Float::compareTo)
                .orElse(null));

        Map<Boolean, List<DonationDto>> partitioned = projects.stream()
                .filter(p -> p.getDonationIds() != null)
                .flatMap(p -> p.getDonationIds().stream())
                .collect(Collectors.partitioningBy(d -> d.getDonationAmount() >= 500.0f));

        dto.setHighDonationCount(partitioned.getOrDefault(true, Collections.emptyList()).size());
        dto.setLowDonationCount(partitioned.getOrDefault(false, Collections.emptyList()).size());
    }

    private static void computeDonorBreakdown(List<Project> projects, ProjectStatsDto dto) {
        dto.setTopDonors(projects.stream()
                .filter(p -> p.getDonationIds() != null)
                .flatMap(p -> p.getDonationIds().stream())
                .filter(p -> p.getStoneEmail() != null)
                .sorted((d1, d2) -> d2.getDonationAmount().compareTo(d1.getDonationAmount()))
                .limit(5)
                .map(d -> d.getStoneEmail() + ": €" + d.getDonationAmount())
                .collect(Collectors.toList()));

        dto.setTotalByDonor(
            projects.stream()
                    .filter(p -> p.getDonationIds() != null)
                    .flatMap(p -> p.getDonationIds().stream())
                    .filter(d -> d.getStoneEmail() != null)
                    .collect(Collectors.toMap(
                            DonationDto::getStoneEmail,
                            DonationDto::getDonationAmount,
                            Float::sum
                    ))
        );

    }

    private static void computeProjectStats(List<Project> projects, ProjectStatsDto dto) {
        dto.setDonationCountByProject(projects.stream()
                .filter(p -> p.getDonationIds() != null) // 🔐 null check to prevent NPE
                .collect(Collectors.toMap(
                        Project::getProjectName,
                        p -> p.getDonationIds().size()
                )));

        dto.setDonationsByProject(
                projects.stream()
                        .filter(p -> p.getDonationIds() != null)
                        .collect(Collectors.toMap(
                                Project::getProjectName,
                                p -> p.getDonationIds().stream()
                                        .map(DonationDto::getDonationAmount)
                                        .reduce(0.0f, Float::sum)
                        ))
        );

        dto.setProjectDurations(projects.stream()
                .filter(p -> p.getProjectCreatedDate() != null && p.getProjectBuildStartDate() != null)
                .collect(Collectors.toMap(
                        Project::getProjectName,
                        p -> {
                            Period duration = Period.between(p.getProjectCreatedDate(), p.getProjectBuildStartDate());
                            return String.format("%d years, %d months, %d days",duration.getYears(), duration.getMonths(), duration.getDays());
                        }
                )));
    }

    private static void mergeDtos(ProjectStatsDto target, ProjectStatsDto... sources) {
        for (ProjectStatsDto src : sources) {
            if (src.getNumberOfDonors()!=null) target.setNumberOfDonors(src.getNumberOfDonors());
            if (src.getDonorTypes() != null) target.setDonorTypes(src.getDonorTypes());
            if (src.getTotalDonations() != null) target.setTotalDonations(src.getTotalDonations());
            if (src.getMinDonation() != null) target.setMinDonation(src.getMinDonation());
            if (src.getMaxDonation() != null) target.setMaxDonation(src.getMaxDonation());
            if (src.getHighDonationCount() != 0) target.setHighDonationCount(src.getHighDonationCount());
            if (src.getLowDonationCount() != 0) target.setLowDonationCount(src.getLowDonationCount());
            if (src.getTopDonors() != null) target.setTopDonors(src.getTopDonors());
            if (src.getTotalByDonor() != null) target.setTotalByDonor(src.getTotalByDonor());
            if (src.getDonationCountByProject() != null) target.setDonationCountByProject(src.getDonationCountByProject());
            if (src.getDonationsByProject() !=null) target.setDonationsByProject(src.getDonationsByProject());
            if (src.getProjectDurations() != null) target.setProjectDurations(src.getProjectDurations());
        }
    }

    public static List<ProjectDonationTotalDto> sortProjectsByDonation(List<Project> projects) {
        return projects.stream()
                .filter(p -> p.getDonationIds() != null)
                .sorted(Comparator.comparingDouble((Project project) ->
                        project.getDonationIds().stream()
                                .map(DonationDto::getDonationAmount)
                                .reduce(0.0f, Float::sum)
                ).reversed()) // Sort by total donation amount (high to low)
                .map(p -> new ProjectDonationTotalDto(
                        p.getProjectName(),
                        p.getDonationIds().stream()
                                .map(DonationDto::getDonationAmount)
                                .reduce(0.0f, Float::sum)
                ))
                .collect(Collectors.toList());
    }


}
