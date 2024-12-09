package com.edee.foundationsforfaith.controllers;

import com.edee.foundationsforfaith.dtos.DonationDto;
import com.edee.foundationsforfaith.dtos.DonationStatsDto;
import com.edee.foundationsforfaith.entities.Donation;
import com.edee.foundationsforfaith.exceptions.UnableToInsertException;
import com.edee.foundationsforfaith.services.DonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @PostMapping("/totalDonations/project")
    public ResponseEntity<?> getTotalDonationsByProjectName (@RequestParam("startDate") String startDate,
                                                             @RequestParam("endDate") String endDate,
                                                             @RequestParam("projectName") String projectName){
        try{
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return new ResponseEntity<DonationStatsDto>(donationService.getTotalDonationsInTimeframe(start, end, projectName), HttpStatus.FOUND);
        } catch (
                UnableToInsertException e){
            return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }




}
