package com.bereznev.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.VacancyDto;
import com.bereznev.exceptions.controller.ExceptionHandler;
import com.bereznev.service.SalaryService;
import com.bereznev.service.VacancyService;
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
@RequestMapping("/api/v1/vacancies")
public class VacanciesController {
    private static final String CONTROLLER_PATH = "/api/v1/vacancies";

    private final VacancyService vacancyService;
    private final SalaryService salaryService;

    public VacanciesController(VacancyService vacancyService, SalaryService salaryService) {
        super();
        this.vacancyService = vacancyService;
        this.salaryService = salaryService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(value = "name", required = false) Optional<String> vacancyName,
            @RequestParam(value = "location", required = false) Optional<String> location) {
        try {
            return new ResponseEntity<>(vacancyService.getAll(vacancyName, location), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, CONTROLLER_PATH);
        }
    }
}
