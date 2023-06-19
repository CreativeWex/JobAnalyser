package com.bereznev.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.ErrorDTO;
import com.bereznev.dto.SalaryDTO;
import com.bereznev.dto.VacancyDTO;
import com.bereznev.service.SalaryService;
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
    private final static String CONTROLLER_PATH = "/api/v1/vacancies";

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
        VacancyDTO vacancyDTO = new VacancyDTO();
        long startTime = System.currentTimeMillis();
        try {
            if (vacancyName.isPresent()) {
                vacancyDTO.setNameFilter(vacancyName.get());
                if (location.isPresent()) {
                    vacancyDTO.setLocationFilter(location.get());
                    vacancyDTO.setVacancies(vacancyService.getAllByNameAndLocation(vacancyName.get(), location.get()));
                } else {
                    vacancyDTO.setVacancies(vacancyService.getAllByName(vacancyName.get()));
                }
            } else {
                vacancyDTO.setVacancies(vacancyService.getAll());
            }
            vacancyDTO.setVacanciesNumber(vacancyDTO.getVacancies().size());
            vacancyDTO.setTimeSpent(System.currentTimeMillis() - startTime + " ms");
            return new ResponseEntity<>(vacancyDTO, HttpStatus.OK);
        } catch (Exception e) {
            ErrorDTO dto = new ErrorDTO();
            dto.setExceptionMessage(e.getMessage());
            dto.setLocalDateTime(LocalDateTime.now());
            dto.setEndpoint(CONTROLLER_PATH);
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/salary_statistics")
        public ResponseEntity<?> getSalaryStatistics(
            @RequestParam(value = "name") String vacancyName,
            @RequestParam(value = "location", required = false) Optional<String> location) {
        long startTime = System.currentTimeMillis();
        try {
            SalaryDTO dto = salaryService.getSalaryStatistics(vacancyName, location);
            dto.setTimeSpent(System.currentTimeMillis() - startTime + " ms");
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            ErrorDTO dto = new ErrorDTO();
            dto.setExceptionMessage(e.getMessage());
            dto.setLocalDateTime(LocalDateTime.now());
            dto.setEndpoint(CONTROLLER_PATH + "/salary_statistics");
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
