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
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setEndpoint("/api/v1/employers");
            errorDTO.setTimestamp(LocalDateTime.now());
            errorDTO.setExceptionMessage(e.getMessage());
            return new ResponseEntity<>(errorDTO, HttpStatus.OK);
        }
    }
}
