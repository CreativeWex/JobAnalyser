package com.bereznev.vacancies.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.vacancies.exception.SendingUrlRequestException;
import com.bereznev.vacancies.model.json_response.ErrorResponse;
import com.bereznev.vacancies.service.EmployerService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public ResponseEntity<?> getEmployersByVacancy(
            @RequestParam("vacancy") String vacancyName,
            @RequestParam(value = "location", required = false) Optional<String> location) {
        try {
            return location.map(s -> new ResponseEntity<>(employerService.getEmployersByVacancy(vacancyName, s), HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(employerService.getEmployersByVacancy(vacancyName), HttpStatus.OK));
        } catch (SendingUrlRequestException exception) {
            ErrorResponse response = new ErrorResponse(
                    LocalDateTime.now(),
                    exception.getResponseCode(),
                    exception.getException().getMessage(),
                    "/api/v1/employers?vacancy=" + vacancyName,
                    exception.getResourceName()
            );
            return new ResponseEntity<>(response, HttpStatus.valueOf(response.getResponseCode()));
        }
    }
}
