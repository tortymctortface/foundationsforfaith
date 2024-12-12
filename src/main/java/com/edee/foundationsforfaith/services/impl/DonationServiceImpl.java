package com.edee.foundationsforfaith.services.impl;

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
import java.util.Optional;

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


    public Donation donateAndAssociateWithProjectAndStone(DonationRecord donationRecord){
        String safeProjectName = (Jsoup.clean(donationRecord.projectName(), Safelist.basic()));
        Optional<Project> project = projectRepository.findProjectByProjectName(safeProjectName);

        if(project.isPresent())
        {
            String safeEmail = (Jsoup.clean(donationRecord.stoneEmail(), Safelist.basic()));
            Optional<Stone> stone = stoneService.getStoneByEmail(safeEmail);
            if(stone.isPresent())
            {
                Donation donation = new Donation();
                donation.setDonorMessage(Jsoup.clean(donationRecord.donorMessage(), Safelist.basic()));
                donation.setDonationAmount(donationRecord.donationAmount());
                donation.setProjectId(project.get().getProjectId().toString());
                donation.setStoneId(stone.get().getStoneId().toString());

                Donation savedDonation = donationRepository.insert(donation);

                mongoTemplate.update(Project.class)
                        .matching(Criteria.where("project_name").is(safeProjectName))
                        .apply(new Update().push("donation_ids").value(savedDonation))
                        .first();

                mongoTemplate.update(Project.class)
                        .matching(Criteria.where("email").is(safeEmail))
                        .apply(new Update().push("donation_ids").value(savedDonation))
                        .first();

                log.info(donationRecord.thanks());
                return savedDonation;

            }else{
                throw new UnableToInsertException("Cannot process donation as a user with the email address : "+ donationRecord.stoneEmail() + " does not exist", HttpStatus.NOT_FOUND);
            }
        }else{
            throw new UnableToInsertException("Cannot process donation as a project with name "+ donationRecord.projectName()+ " does  not exist", HttpStatus.NOT_FOUND);
        }
    }

    public DonationStatsDto getTotalDonations(String projectName){
        Integer totalDonationAmount = 0;
        int index = 0;
        Project project = projectService.getDefensiveProjectByProjectName(projectName);
        var donations = donationRepository.findAllByProjectId(project.getProjectId().toString());
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
        return donationStatsDto;
    }

    public DonationStatsDto getTotalDonations(Date start, Date end, String projectName){
        Integer totalDonationAmount = 0;
        int index = 0;
        Project project = projectService.getDefensiveProjectByProjectName(projectName);
        var donations = donationRepository.findAllBetweenDates(start, end, project.getProjectId().toString());
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
