package com.bereznev.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

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
    private final static String CONTROLLER_PATH = "/api/v1/employers";

    private final EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        super();
        this.employerService = employerService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(value = "vacancy", required = false) Optional<String> vacancyName,
            @RequestParam(value = "location", required = false) Optional<String> location) {
        try {
            if (vacancyName.isPresent() && location.isPresent()) {
                return new ResponseEntity<>(employerService.getAllFilteredByVacancyAndLocation(vacancyName.get(), location.get()), HttpStatus.OK);
            } else if (vacancyName.isPresent()) {
                return new ResponseEntity<>(employerService.getAllFilteredByVacancy(vacancyName.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(employerService.getAll(), HttpStatus.OK);
            }
        } catch (Exception e) {
            ErrorDTO dto = new ErrorDTO();
            dto.setExceptionMessage(e.getMessage());
            dto.setLocalDateTime(LocalDateTime.now());
            dto.setEndpoint(CONTROLLER_PATH);
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
