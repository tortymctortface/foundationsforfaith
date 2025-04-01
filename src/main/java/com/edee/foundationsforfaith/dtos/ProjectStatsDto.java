package com.edee.foundationsforfaith.dtos;

import com.edee.foundationsforfaith.enums.StoneType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectStatsDto {
    private List<StoneType> stoneTypes;
    private Long stoneCount;
    private Float totalDonations;
    private List<String> topDonors;
    private Float minDonation;
    private Float maxDonation;
    private Map<String, Float> totalByDonor;
    private Map<String, Integer> donationCountByProject;
    private Map<String, Float> donationsByProject;
    private int highDonationCount;
    private int lowDonationCount;
    private Map<String, String> projectDurations;

}
