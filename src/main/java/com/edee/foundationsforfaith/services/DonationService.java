package com.edee.foundationsforfaith.services;

import com.edee.foundationsforfaith.dtos.DonationDto;
import com.edee.foundationsforfaith.entities.Donation;

public interface DonationService {
    Donation donateAndAssociateWithProjectAndStone(DonationDto donationDto);
}
