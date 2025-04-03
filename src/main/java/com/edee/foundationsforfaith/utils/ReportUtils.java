package com.edee.foundationsforfaith.utils;

import com.edee.foundationsforfaith.dtos.DonationDto;
import com.edee.foundationsforfaith.entities.Donation;
import com.edee.foundationsforfaith.entities.Project;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class ReportUtils {

    public static Path writeLocalizedProjectReport(Project project, Locale locale) throws IOException {
        String safeProjectName = project.getProjectName().replaceAll("[^a-zA-Z0-9\\-_]", "_");
        String langSuffix = locale.getLanguage();
        Path projectDir = Paths.get("reports", safeProjectName);
        if (Files.notExists(projectDir)) {
            Files.createDirectories(projectDir);
        }

        Path file = projectDir.resolve("project-stats_" + langSuffix + ".txt");

        List<String> lines = buildLocalizedReport(project, locale);

        if (Files.exists(file)) {
            List<String> existing = Files.readAllLines(file);
            if (existing.equals(lines)) {
                log.info("No change for localized report [{}]", file.getFileName());
                return file;
            }
        }

        Files.write(file, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        log.info("Localized report written: {}", file.toAbsolutePath());
        return file;
    }

    public static List<String> buildLocalizedReport(Project project, Locale locale) {
        long donorCount = project.getDonationIds().stream()
                .filter(d -> d.getStoneEmail() != null)
                .map(DonationDto::getStoneEmail)
                .distinct()
                .count();

        Float totalDonations = project.getDonationIds().stream()
                .map(DonationDto::getDonationAmount)
                .reduce(0.0f, Float::sum);

        SequencedSet<String> topDonors = new LinkedHashSet<>(
                project.getDonationIds().stream()
                        .filter(d -> d.getStoneEmail() != null)
                        .sorted(Comparator.comparing(DonationDto::getDonationAmount).reversed())
                        .limit(5)
                        .map(d -> d.getStoneEmail() + ": €" + d.getDonationAmount())
                        .toList()
        );

        // Example use of 'var _' (Java 22): silently looping for logging/testing without using the value
        topDonors.forEach(_ -> log.debug("Donor processed"));

        String duration = (project.getProjectCreatedDate() != null && project.getProjectBuildStartDate() != null)
                ? formatPeriod(project.getProjectCreatedDate(), project.getProjectBuildStartDate())
                : "N/A";

        return List.of(
                LocalizationUtils.format("project.report.title", locale, project.getProjectName()),
                LocalizationUtils.format("project.report.donors", locale, donorCount),
                LocalizationUtils.format("project.report.donations", locale, totalDonations),
                LocalizationUtils.format("project.report.topDonors", locale, topDonors),
                LocalizationUtils.format("project.report.duration", locale, duration)
        );
    }

    private static String formatPeriod(LocalDate start, LocalDate end) {
        Period period = Period.between(start, end);
        return String.format("%d months, %d days", period.getMonths(), period.getDays());
    }
}