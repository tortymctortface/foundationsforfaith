package com.edee.foundationsforfaith.services.impl;

import com.edee.foundationsforfaith.dtos.DonationDto;
import com.edee.foundationsforfaith.dtos.DonationStatsDto;
import com.edee.foundationsforfaith.entities.Donation;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.Stone;
import com.edee.foundationsforfaith.exceptions.UnableToInsertException;
import com.edee.foundationsforfaith.records.DonationRecord;
import com.edee.foundationsforfaith.repositories.DonationRepository;
import com.edee.foundationsforfaith.repositories.ProjectRepository;
import com.edee.foundationsforfaith.repositories.StoneRepository;
import com.edee.foundationsforfaith.services.DonationService;
import com.edee.foundationsforfaith.services.ProjectService;
import com.edee.foundationsforfaith.services.StoneService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Log4j2
@Service
public class DonationServiceImpl implements DonationService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private StoneRepository stoneRepository;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private StoneService stoneService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Qualifier("renovationServiceImpl")
    @Autowired
    private ProjectService projectService;

    private final Predicate<Donation> isValidDonation = d ->
            d.getDonationAmount() != null &&
                    d.getStoneId().getDonorName() != null &&
                    d.getDonationAmount().compareTo(0.0f) > 0;

    private final Supplier<RuntimeException> invalidDonationException = () ->
            new IllegalArgumentException("Invalid donation data");

    private final Consumer<Donation> logDonation = d ->
            log.info("Processing donation from: " + d.getStoneId().getDonorName() + " for $" + d.getDonationAmount());

    private final Function<Donation, DonationDto> donationToDto = d ->
            new DonationDto(
                    d.getProjectId().getProjectName(),
                    d.getDonationAmount(),
                    d.getStoneId().getDonorName(),
                    d.getStoneId().getEmail()
            );


    public DonationDto donateAndAssociateWithProjectAndStone(DonationRecord donationRecord){
        Project project = projectRepository.findProjectByProjectName(donationRecord.projectName())
                .orElseThrow(() -> new UnableToInsertException(
                        "Cannot process donation as a user with the email address: " + donationRecord.projectName() + " does not exist.",
                        HttpStatus.NOT_FOUND
                ));

        Stone stone = stoneRepository.findStoneByEmail(donationRecord.stoneEmail())
                .orElseThrow(() -> new UnableToInsertException(
                        "Cannot process donation as a donor with email "+ donationRecord.stoneEmail()+ " does  not exist",
                        HttpStatus.NOT_FOUND
                ));

        Donation donation = new Donation();
        donation.setStoneId(stone);
        donation.setProjectId(project);
        donation.setDonationAmount(donationRecord.donationAmount());
        donation.setDonationCreationDate(LocalDate.now());
        donation.setDonorMessage(donationRecord.donorMessage());

        if (!isValidDonation.test(donation)) {
            throw invalidDonationException.get();
        }

        logDonation.accept(donation);

        Donation saved = donationRepository.save(donation);

        return donationToDto.apply(saved);
    }

    public DonationStatsDto getTotalDonations(String projectName){
        Project project = projectService.getDefensiveProjectByProjectName(projectName);
        var donations = donationRepository.findAllByProjectId(project.getProjectId().toString());
        return getDonationStats(donations, project);
    }

    public DonationStatsDto getTotalDonations(Date start, Date end, String projectName){
        Project project = projectService.getDefensiveProjectByProjectName(projectName);
        var donations = donationRepository.findAllBetweenDates(start, end, project.getProjectId().toString());
        return getDonationStats(donations, project);
    }

    private DonationStatsDto getDonationStats (List<Donation> donations, Project project){
        Float totalDonationAmount = 0f;
        int index = 0;
        float[] donationAmounts = new float[donations.size()];
        for(var donation : donations){
            if(ProjectService.isValidDonationAmount(donation.getDonationAmount())) {
                totalDonationAmount += donation.getDonationAmount();
                donationAmounts[index] = donation.getDonationAmount();
                index++;
            }
        }
        DonationStatsDto donationStatsDto = new DonationStatsDto();
        donationStatsDto.setProjectFunding(totalDonationAmount);
        getAverageDonationAmountPerProject(donationStatsDto, donationAmounts);
        donationStatsDto.setUserMessage(project.getProjectName(), totalDonationAmount, project.getAmountOfFundingRequired());
        return donationStatsDto;
    }

    public void getAverageDonationAmountPerProject(DonationStatsDto donationStatsDto, float[] donations){
        // Calling the default method
        float averageRating = projectService.getAverageRating(donations);
        log.info("length = " + donations.length);
        log.info("length = " + Arrays.toString(donations));
        donationStatsDto.setProjectAverageDonation(averageRating);
    }
}
