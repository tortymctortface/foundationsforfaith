package com.edee.foundationsforfaith.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DonationDto {
    private String donorMessage;
    private Float donationAmount;
    private String projectName;
    private String stoneEmail;
}
