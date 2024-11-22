package com.edee.foundationsforfaith.services.impl;

import com.edee.foundationsforfaith.dtos.DonationDto;
import com.edee.foundationsforfaith.dtos.StoneCreationDto;
import com.edee.foundationsforfaith.entities.Donation;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.Stone;
import com.edee.foundationsforfaith.exceptions.UnableToInsertException;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    private ProjectService projectService;

    @Autowired
    private StoneService stoneService;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Donation donateAndAssociateWithProjectAndStone(DonationDto donationDto){
        String safeProjectName = (Jsoup.clean(donationDto.getProjectName(), Safelist.basic()));
        Optional<Project> project = projectService.getProjectByProjectName(safeProjectName);

        if(project.isPresent())
        {
            String safeEmail = (Jsoup.clean(donationDto.getStoneEmail(), Safelist.basic()));
            Optional<Stone> stone = stoneService.getStoneByEmail(safeEmail);
            if(stone.isPresent())
            {
                Donation donation = new Donation();
                donation.setDonorMessage(Jsoup.clean(donationDto.getDonorMessage(), Safelist.basic()));
                donation.setDonationAmount(donationDto.getDonationAmount());
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

                return savedDonation;

            }else{
                throw new UnableToInsertException("Cannot process donation as a user with the email address : "+ donationDto.getStoneEmail() + " does not exist", HttpStatus.NOT_FOUND);
            }
        }else{
            throw new UnableToInsertException("Cannot process donation as a project with name "+ donationDto.getProjectName()+ " does  not exist", HttpStatus.NOT_FOUND);
        }
    }

}
