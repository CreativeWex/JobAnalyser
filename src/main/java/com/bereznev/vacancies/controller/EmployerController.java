package com.bereznev.vacancies.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.vacancies.entity.Employer;
import com.bereznev.vacancies.service.EmployerService;
import com.bereznev.vacancies.service.VacancyService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Log4j
@RestController
@RequestMapping("/api/v1/employers")
public class EmployerController {

    private final EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        super();
        this.employerService = employerService;
    }

    @GetMapping
    public ResponseEntity<Set<Employer>> getEmployersByVacancy(@RequestParam("text") String vacancyName) {
        return new ResponseEntity<>(employerService.getEmployersByVacancy(vacancyName), HttpStatus.OK);
    }
}
