package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.DonationStatsDto;
import com.edee.foundationsforfaith.entities.Donation;
import com.edee.foundationsforfaith.records.DonationRecord;

import java.time.LocalDate;
import java.util.Date;

public interface DonationService {
    Donation donateAndAssociateWithProjectAndStone(DonationRecord donationRecord);

    DonationStatsDto getTotalDonations(Date start, Date end, String projectName);

    DonationStatsDto getTotalDonations(String projectName);
}
