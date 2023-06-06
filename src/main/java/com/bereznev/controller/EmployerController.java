package com.bereznev.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.exception.SendingUrlRequestException;
import com.bereznev.dto.ErrorDTO;
import com.bereznev.service.EmployerService;
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

// TODO: рефакторинг

    @GetMapping
    public ResponseEntity<?> getEmployersByVacancy(
            @RequestParam(value = "vacancy", required = false) Optional<String> vacancyName,
            @RequestParam(value = "location", required = false) Optional<String> location) {
        try {
            if (vacancyName.isPresent() && location.isEmpty()) {
                return new ResponseEntity<>(employerService.getEmployersByVacancy(vacancyName.get()), HttpStatus.OK);
            } else if (vacancyName.isPresent() && location.isPresent()) {
                return new ResponseEntity<>(employerService.getEmployersByVacancy(vacancyName.get(), location.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(employerService.getAll(), HttpStatus.OK);
            }
        } catch (SendingUrlRequestException exception) {
            ErrorDTO response = new ErrorDTO(
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
