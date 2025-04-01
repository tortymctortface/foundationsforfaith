package com.edee.foundationsforfaith.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectStatsDto {
    private long totalStones;
    private List<String> stoneTypes;
    private List<String> stoneCount;
    private BigDecimal totalDonations;
    private List<String> topDonors;
    private BigDecimal minDonation;
    private BigDecimal maxDonation;
    private Map<String, BigDecimal> totalByDonor;
    private Map<String, Integer> donationCountByProject;
    private int highDonationCount;
    private int lowDonationCount;
    private Map<String, String> projectDurations;

}
