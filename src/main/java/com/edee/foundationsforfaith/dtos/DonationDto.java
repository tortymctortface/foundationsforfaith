package com.edee.foundationsforfaith.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DonationDto {
    private String donorMessage;
    private Integer donationAmount;
    private String projectName;
    private String stoneEmail;
}
