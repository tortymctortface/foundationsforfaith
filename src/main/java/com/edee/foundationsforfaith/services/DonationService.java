package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.DonationDto;
import com.edee.foundationsforfaith.dtos.DonationStatsDto;
import com.edee.foundationsforfaith.entities.Donation;

import java.time.LocalDate;
import java.util.Date;

public interface DonationService {
    Donation donateAndAssociateWithProjectAndStone(DonationDto donationDto);

    DonationStatsDto getTotalDonationsInTimeframe(Date start, Date end, String projectName);
}
