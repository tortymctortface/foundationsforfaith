package com.edee.foundationsforfaith.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectDonationTotalDto {
    private String projectName;
    private BigDecimal totalDonations;
}
