package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.DonationDto;
import com.edee.foundationsforfaith.entities.Donation;

import java.time.LocalDate;

public interface DonationService {
    Donation donateAndAssociateWithProjectAndStone(DonationDto donationDto);

    Integer getTotalDonationsInTimeframe(LocalDate start, LocalDate end, String projectName);
}
