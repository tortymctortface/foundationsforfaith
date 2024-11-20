package com.edee.foundationsforfaith.services.impl;

import com.edee.foundationsforfaith.dtos.ProjectUpdateCreationDto;
import com.edee.foundationsforfaith.dtos.StoneCreationDto;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.ProjectUpdate;
import com.edee.foundationsforfaith.entities.Stone;
import com.edee.foundationsforfaith.enums.ProjectUpdateType;
import com.edee.foundationsforfaith.exceptions.UnableToInsertException;
import com.edee.foundationsforfaith.repositories.ProjectRepository;
import com.edee.foundationsforfaith.repositories.ProjectUpdateRepository;
import com.edee.foundationsforfaith.repositories.StoneRepository;
import com.edee.foundationsforfaith.services.ProjectService;
import com.edee.foundationsforfaith.services.StoneService;
import com.edee.foundationsforfaith.utils.EnumUtils;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
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

import static com.edee.foundationsforfaith.enums.ProjectUpdateType.GENERAL_UPDATE;

@Log4j2
@Service
public class StoneServiceImpl implements StoneService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private StoneRepository stoneRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Stone createStoneToAssociateWithProject(StoneCreationDto stoneCreationDto){
        String safeProjectName = (Jsoup.clean(stoneCreationDto.getProjectName(), Safelist.basic()));
        Optional<Project> project = projectService.getProjectByProjectName(safeProjectName);

        if(project.isPresent()) {
            if (stoneCreationDto.getEmailAddress() != null && !stoneCreationDto.getEmailAddress().isBlank()){
                Stone stone = new Stone();
                stone.setEmail(Jsoup.clean(stoneCreationDto.getEmailAddress(), Safelist.basic()));
                stone.setDonorName(Jsoup.clean(stoneCreationDto.getDonorName(), Safelist.basic()));
                stone.setSendUpdatesToDonor(stoneCreationDto.getSendUpdatesToDonor());
                List<String> stonesFirstProject = new ArrayList<>();
                stonesFirstProject.add(project.get().getProjectId().toString());
                stone.setProjectIds(stonesFirstProject);
                Stone savedStone = stoneRepository.insert(stone);

                mongoTemplate.update(Project.class)
                        .matching(Criteria.where("project_name").is(safeProjectName))
                        .apply(new Update().push("stone_ids").value(savedStone))
                        .first();

                return savedStone;

            }else{
                throw new UnableToInsertException("Cannot create without a valid email address : "+ stoneCreationDto.getEmailAddress(), HttpStatus.BAD_REQUEST);
            }
        }else{
            throw new UnableToInsertException("Cannot create as a project with name "+ stoneCreationDto.getProjectName()+ " does  not exist", HttpStatus.NOT_FOUND);
        }
    }
}
