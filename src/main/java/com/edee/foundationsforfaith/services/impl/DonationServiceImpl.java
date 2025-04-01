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
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
            d.getAmount() != null &&
                    d.getDonorName() != null &&
                    d.getAmount().compareTo(BigDecimal.ZERO) > 0;

    private final Supplier<RuntimeException> invalidDonationException = () ->
            new IllegalArgumentException("Invalid donation data");

    private final Consumer<Donation> logDonation = d ->
            log.info("Processing donation from: " + d.getDonorName() + " for $" + d.getAmount());

    private final Function<Donation, DonationDto> donationToDto = d ->
            new DonationDto(
                    d.getId(),
                    d.getAmount(),
                    d.getDonorName(),
                    d.getDateCreated()
            );


    public DonationDto donateAndAssociateWithProjectAndStone(Donation donation){
        Optional<Project> project = projectRepository.findProjectByProjectName(donation.projectName())
                .isNotPresent(() -> new UnableToInsertException("Cannot process donation as a user with the email address : "+ donationRecord.stoneEmail() + " does not exist", HttpStatus.NOT_FOUND));

        Optional<Stone> stone = stoneService.getStoneByEmail(donation.stoneEmail())
                .isNotPresent(() ->  new UnableToInsertException("Cannot process donation as a project with name "+ donationRecord.projectName()+ " does  not exist", HttpStatus.NOT_FOUND));

        if (!isValidDonation.test(donation)) {
            throw invalidDonationException.get();
        }

        logDonation.accept(donation);

        donation.setProject(project);
        donation.setStone(stone);

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
        Integer totalDonationAmount = 0;
        int index = 0;
        int[] donationAmounts = new int[donations.size()];
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

    public void getAverageDonationAmountPerProject(DonationStatsDto donationStatsDto, int[] donations){
        // Calling the default method
        double averageRating = projectService.getAverageRating(donations);
        log.info("length = " + donations.length);
        log.info("length = " + Arrays.toString(donations));
        donationStatsDto.setProjectAverageDonation(averageRating);
    }
}
