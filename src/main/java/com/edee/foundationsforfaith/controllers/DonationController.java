package com.edee.foundationsforfaith.controllers;

import com.edee.foundationsforfaith.dtos.DonationDto;
import com.edee.foundationsforfaith.dtos.DonationStatsDto;
import com.edee.foundationsforfaith.entities.Donation;
import com.edee.foundationsforfaith.exceptions.UnableToInsertException;
import com.edee.foundationsforfaith.records.DonationRecord;
import com.edee.foundationsforfaith.services.DonationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/api")
@Log4j2
public class DonationController {

    @Autowired
    DonationService donationService;

    @PostMapping("/create/donation")
    public ResponseEntity<?> processDonation (@RequestBody DonationDto donationDto){
        try{
            log.info("length = " );

            DonationRecord donationRecord = new DonationRecord(donationDto.getDonorMessage(), donationDto.getDonationAmount(), donationDto.getProjectName(), donationDto.getStoneEmail());
            log.info("length = " );
            return new ResponseEntity<Donation>(donationService.donateAndAssociateWithProjectAndStone(donationRecord), HttpStatus.CREATED);
        } catch (
                UnableToInsertException e){
            return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/totalDonations/project")
    public ResponseEntity<?> getTotalDonationsByProjectName(@RequestParam("startDate") String startDate,
                                                             @RequestParam("endDate") String endDate,
                                                             @RequestParam("projectName") String projectName) {

        try {
            if (endDate ==null || startDate == null || endDate.isBlank() || startDate.isBlank()) {
                return new ResponseEntity<DonationStatsDto>(donationService.getTotalDonations(projectName), HttpStatus.FOUND);
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date start = format.parse(startDate);
                Date end = format.parse(endDate);
                return new ResponseEntity<DonationStatsDto>(donationService.getTotalDonations(start, end, projectName), HttpStatus.FOUND);
            }
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (UnableToInsertException e){
            return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        }

    }

}
