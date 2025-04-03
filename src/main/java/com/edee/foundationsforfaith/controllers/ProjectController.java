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
import com.edee.foundationsforfaith.exceptions.UnableToInsertException;
import com.edee.foundationsforfaith.repositories.ProjectRepository;
import com.edee.foundationsforfaith.services.ProjectService;
import com.edee.foundationsforfaith.services.ProjectUpdateService;
import com.edee.foundationsforfaith.utils.ReportUtils;
import com.edee.foundationsforfaith.utils.StatisticUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService newBuildServiceImpl;
    private final ProjectService renovationServiceImpl;
    private final ProjectRepository projectRepository;
    private final ProjectUpdateService projectUpdateService;
    @Autowired
    public ProjectController(@Qualifier("newBuildServiceImpl") ProjectService newBuildServiceImpl,
                             @Qualifier("renovationServiceImpl") ProjectService renovationServiceImpl,
                             ProjectRepository projectRepository,
                             ProjectUpdateService projectUpdateService
    ) {
        this.newBuildServiceImpl = newBuildServiceImpl;
        this.renovationServiceImpl = renovationServiceImpl;
        this.projectRepository = projectRepository;
        this.projectUpdateService = projectUpdateService;
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> allProjects(){
        return new ResponseEntity<List<Project>>(projectRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/project/name/")
    public ResponseEntity<Optional<Project>> projectById(@RequestParam String projectName){
        String safeProjectName = Jsoup.clean(projectName, Safelist.basic());
        return new ResponseEntity<Optional<Project>>(projectRepository.findProjectByProjectName(safeProjectName), HttpStatus.OK);
    }

    @PostMapping("/create/project")
    public ResponseEntity<?> createProject(@RequestBody ProjectCreationDto dto) {
        try {
            Project project = switch (dto.getProjectType()) {
                case  "NEW_BUILD" -> newBuildServiceImpl.createProject(dto);
                case "RENOVATION" -> renovationServiceImpl.createProject(dto);
                default -> throw new IllegalArgumentException("Invalid project type");
            };

            return new ResponseEntity<>(project, HttpStatus.CREATED);

        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Duplicate project name used. Error: " + e.getMessage());
        }
    }

    @GetMapping("/projects/report-localized")
    public ResponseEntity<String> generateLocalizedProjectReport(
            @RequestParam String name,
            @RequestParam(defaultValue = "en") String lang
    ) throws IOException {
        Project project = projectRepository.findProjectByProjectName(name)
                .orElseThrow(() -> new RuntimeException("Project not found"));

                        Locale locale = Locale.forLanguageTag(lang);
        Path file = ReportUtils.writeLocalizedProjectReport(project, locale);

        return ResponseEntity.ok("Localized report written to: " + file.toAbsolutePath());
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
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        ProjectAction action = switch (dto.getType().toUpperCase()) {
            case "START" -> new StartProject(project.getProjectName());
            case "PAUSE" -> new PauseProject(project.getProjectName());
            case "COMPLETE" -> new CompleteProject(project.getProjectName());
            default -> throw new IllegalArgumentException("Invalid action type");
        };

        try{
            projectUpdateService.handleAction(action, project);
        } catch (Exception e) {

        }
        projectUpdateService.handleAction(action, project);

        return ResponseEntity.ok("Action recorded: " + dto.getType());
    }

    @GetMapping("/projects/sorted-by-donations")
    public ResponseEntity<List<ProjectDonationTotalDto>> getProjectsSortedByDonations() {
        List<Project> projects = projectRepository.findAll();
        List<ProjectDonationTotalDto> sorted = StatisticUtils.sortProjectsByDonation(projects);
        return ResponseEntity.ok(sorted);
    }


}
