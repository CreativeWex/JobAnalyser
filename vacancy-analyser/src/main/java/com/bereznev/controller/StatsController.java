package com.bereznev.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.exceptions.controller.ExceptionHandler;
import com.bereznev.service.SalaryService;
import com.bereznev.service.SkillService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Log4j
@RestController
@RequestMapping("/api/v1/stats")
public class StatsController {
    private static final String CONTROLLER_PATH = "/api/v1/stats";

    private final SalaryService salaryService;
    private final SkillService skillService;

    public StatsController(SalaryService salaryService, SkillService skillService) {
        super();
        this.salaryService = salaryService;
        this.skillService = skillService;
    }

    @GetMapping("/skills")
    public ResponseEntity<?> getMostPopularSkills(
            @RequestParam(value = "vacancy_name", required = false) Optional<String> vacancyName,
            @RequestParam(value = "location", required = false) Optional<String> location,
            @RequestParam(value = "amount", required = false) Optional<Integer> amount) {
        try {
            return new ResponseEntity<>(skillService.getMostPopularSkills(vacancyName, location, amount), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, CONTROLLER_PATH + "/skills");
        }
    }

    @GetMapping("/salary")
    public ResponseEntity<?> getSalaryStatistics(
            @RequestParam(value = "vacancy_name") String vacancyName,
            @RequestParam(value = "location", required = false) Optional<String> location) {
        try {
            return new ResponseEntity<>(salaryService.getSalaryStatistics(vacancyName, location), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, CONTROLLER_PATH + "/salary_statistics");
        }
    }
}
