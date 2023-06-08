package com.bereznev.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.ErrorDTO;
import com.bereznev.exception.SendingUrlRequestException;
import com.bereznev.service.VacancyService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@Log4j
@RestController
@RequestMapping("/api/v1/vacancies")
public class VacanciesController {

    private final VacancyService vacancyService;

    public VacanciesController(VacancyService vacancyService) {
        super();
        this.vacancyService = vacancyService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(value = "vacancy") String vacancyName,
            @RequestParam(value = "location", required = false) Optional<String> location) {
        try {
            if (location.isPresent()) {
                return null; //TODO
            } else {
                return new ResponseEntity<>(vacancyService.getVacanciesByName(vacancyName), HttpStatus.OK);
            }
        } catch (SendingUrlRequestException e) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setResponseCode(e.getResponseCode());
            errorDTO.setEndpoint("/api/v1/vacancies");
            errorDTO.setTimestamp(LocalDateTime.now());
            errorDTO.setExceptionMessage(e.getMessage());
            errorDTO.setResourceName(e.getResourceName());
            return new ResponseEntity<>(errorDTO, HttpStatus.OK);
        }
    }
}
