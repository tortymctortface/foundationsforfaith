package com.edee.foundationsforfaith.controllers;

import com.edee.foundationsforfaith.dtos.DonationDto;
import com.edee.foundationsforfaith.entities.Donation;
import com.edee.foundationsforfaith.exceptions.UnableToInsertException;
import com.edee.foundationsforfaith.services.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DonationController {

    @Autowired
    DonationService donationService;

    @PostMapping("/create/donation")
    public ResponseEntity<?> processDonation (@RequestBody DonationDto donationDto){
        try{
            return new ResponseEntity<Donation>(donationService.donateAndAssociateWithProjectAndStone(donationDto), HttpStatus.CREATED);
        } catch (
                UnableToInsertException e){
            return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
