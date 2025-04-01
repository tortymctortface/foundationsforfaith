package com.edee.foundationsforfaith.controllers;

import com.edee.foundationsforfaith.dtos.ProjectActionDto;
import com.edee.foundationsforfaith.dtos.ProjectCreationDto;
import com.edee.foundationsforfaith.dtos.ProjectDonationTotalDto;
import com.edee.foundationsforfaith.dtos.ProjectStatsDto;
import com.edee.foundationsforfaith.entities.Project;
import com.edee.foundationsforfaith.entities.actions.CompleteProject;
import com.edee.foundationsforfaith.entities.actions.PauseProject;
import com.edee.foundationsforfaith.entities.actions.ProjectAction;
import com.edee.foundationsforfaith.entities.actions.StartProject;
import com.edee.foundationsforfaith.repositories.ProjectRepository;
import com.edee.foundationsforfaith.services.ProjectService;
import com.edee.foundationsforfaith.utils.ProjectActionUtils;
import com.edee.foundationsforfaith.utils.StatisticUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService newBuildServiceImpl;
    private final ProjectService renovationServiceImpl;
    private final ProjectRepository projectRepository;
    @Autowired
    public ProjectController(@Qualifier("newBuildServiceImpl") ProjectService newBuildServiceImpl,
                             @Qualifier("renovationServiceImpl") ProjectService renovationServiceImpl,
                             ProjectRepository projectRepository) {
        this.newBuildServiceImpl = newBuildServiceImpl;
        this.renovationServiceImpl = renovationServiceImpl;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> allProjects(){
        return new ResponseEntity<List<Project>>(projectRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/project/{name}")
    public ResponseEntity<Optional<Project>> projectById(@PathVariable String projectName){
        String safeProjectName = Jsoup.clean(projectName, Safelist.basic());
        return new ResponseEntity<Optional<Project>>(projectRepository.findProjectByProjectName(safeProjectName), HttpStatus.OK);
    }

    @PostMapping("/create/project")
    public ResponseEntity<?> createProject(@RequestBody ProjectCreationDto dto) {
        try {
            Project project = switch (dto.getProjectType()) {
                case NEW_BUILD -> newBuildServiceImpl.createProject(dto);
                case RENOVATION -> renovationServiceImpl.createProject(dto);
            };

            return new ResponseEntity<>(project, HttpStatus.CREATED);

        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Duplicate project name used. Error: " + e.getMessage());
        }
    }


    @GetMapping("/projects/stats")
    public ResponseEntity<ProjectStatsDto> getProjectStats() {
        List<Project> projects = projectRepository.findAll();
        ProjectStatsDto stats = StatisticUtils.analyzeProjects(projects);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/projects/stats/concurrent")
    public ResponseEntity<ProjectStatsDto> getConcurrentStats() throws Exception {
        List<Project> projects = projectRepository.findAll();
        ProjectStatsDto dto = StatisticUtils.analyzeProjectsConcurrent(projects);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/projects/action")
    public ResponseEntity<String> actOnProject(@RequestBody ProjectActionDto dto) {
        String safeProjectName = Jsoup.clean(dto.getProjectName(), Safelist.basic());
        Project project = projectRepository.findProjectByProjectName(safeProjectName)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        ProjectAction action = switch (dto.getType().toUpperCase()) {
            case "START" -> new StartProject(project.getName());
            case "PAUSE" -> new PauseProject(project.getName());
            case "COMPLETE" -> new CompleteProject(project.getName());
            default -> throw new IllegalArgumentException("Invalid action type");
        };

        ProjectActionUtils.handleAction(action, project);

        return ResponseEntity.ok("Action recorded: " + dto.getType());
    }

    @GetMapping("/projects/sorted-by-donations")
    public ResponseEntity<List<ProjectDonationTotalDto>> getProjectsSortedByDonations() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDonationTotalDto> sorted = StatisticUtils.sortProjectsByDonation(projects);
        return ResponseEntity.ok(sorted);
    }


}
