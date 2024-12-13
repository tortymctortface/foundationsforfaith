package com.edee.foundationsforfaith.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DonationStatsDto {
    private int projectFunding;
    private double projectAverageDonation;
    private String userMessage;

    public void setUserMessage(String projectName, Integer fundingAcquired, Integer amountOfFundingRequired) {
        this.userMessage = projectName + " has had a total amount of funding of : "+fundingAcquired+" throughout its whole lifecycle. It requires "+amountOfFundingRequired+" to be fully funded.";
    }
}
