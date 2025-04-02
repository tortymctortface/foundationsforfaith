package com.edee.foundationsforfaith.utils;

import com.edee.foundationsforfaith.entities.Donation;
import com.edee.foundationsforfaith.entities.Project;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;

@Log4j2
public class ReportUtils {

    public static Path writeSingleProjectReport(Project project) throws IOException {
        String safeProjectName = project.getProjectName().replaceAll("[^a-zA-Z0-9\\-_]", "_");
        Path projectDir = Paths.get("reports", safeProjectName);
        if (Files.notExists(projectDir)) {
            Files.createDirectories(projectDir);
        }

        Path file = projectDir.resolve("project-stats.txt");

        long totalStones = project.getStoneIds().size();

        Float totalDonations = project.getDonationIds().stream()
                .map(Donation::getDonationAmount)
                .reduce(0.0f, Float::sum);

        List<String> topDonors = project.getDonationIds().stream()
                .filter(d -> d.getStoneId().getDonorName() != null)
                .sorted((d1, d2) -> d2.getDonationAmount().compareTo(d1.getDonationAmount()))
                .limit(5)
                .map(d -> d.getStoneId().getDonorName() + ": €" + d.getDonationAmount()).toList();

        String duration = (project.getProjectCreatedDate() != null && project.getProjectBuildStartDate() != null)
                ? formatPeriod(project.getProjectCreatedDate(), project.getProjectBuildStartDate())
                : "N/A";

        List<String> reportLines = List.of(
                "=== Project Report: " + project.getProjectName() + " ===",
                "Total Stones: " + totalStones,
                "Total Donations: $" + totalDonations,
                "Top Donors: " + topDonors,
                "Duration: " + duration
        );

        // Only write if content has changed
        if (Files.exists(file)) {
            List<String> existing = Files.readAllLines(file);
            if (existing.equals(reportLines)) {
                log.info("No changes for project '{}', skipping write.", project.getProjectName());
                return file;
            }
        }

        Files.write(file, reportLines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        log.info("Project report written: {}", file.toAbsolutePath());

        return file;
    }

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
        var msg = LocalizationUtils::format;

        long totalStones = project.getStoneIds().size();

        Float totalDonations = project.getDonationIds().stream()
                .map(Donation::getDonationAmount)
                .reduce(0.0f, Float::sum);

        List<String> topDonors = project.getDonationIds().stream()
                .filter(d -> d.getStoneId().getDonorName() != null)
                .sorted((d1, d2) -> d2.getDonationAmount().compareTo(d1.getDonationAmount()))
                .limit(5)
                .map(d -> d.getStoneId().getDonorName() + ": €" + d.getDonationAmount()).toList();

        String duration = (project.getProjectCreatedDate() != null && project.getProjectBuildStartDate() != null)
                ? formatPeriod(project.getProjectCreatedDate(), project.getProjectBuildStartDate())
                : "N/A";

        List<String> reportLines = List.of(
                "=== Project Report: " + project.getProjectName() + " ===",
                "Total Stones: " + totalStones,
                "Total Donations: $" + totalDonations,
                "Top Donors: " + topDonors,
                "Duration: " + duration
        );

        return List.of(
                msg.apply("project.report.title", locale, project.getProjectName()),
                msg.apply("project.report.donors", locale, totalStones),
                msg.apply("project.report.donations", locale, totalDonations),
                msg.apply("project.report.topDonors", locale, topDonors),
                msg.apply("project.report.duration", locale, duration)
        );
    }

    private static String formatPeriod(LocalDate start, LocalDate end) {
        Period period = Period.between(start, end);
        return String.format("%d months, %d days", period.getMonths(), period.getDays());
    }
}


