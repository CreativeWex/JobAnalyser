package com.bereznev.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.exceptions.controller.ExceptionHandler;
import com.bereznev.service.EmployerInitializer;
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
@RequestMapping("/api/v1/data")
public class DataInitializerController {
    private final EmployerInitializer employerInitializer;

    private static final String CONTROLLER_PATH = "/api/v1/data";

    public DataInitializerController(EmployerInitializer employerInitializer) {
        super();
        this.employerInitializer = employerInitializer;
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshData(
            @RequestParam(value = "vacancy_name", required = false) Optional<String> vacancyName,
            @RequestParam(value = "location", required = false) Optional<String> location,
            @RequestParam(value = "pages_amount", required = false) Optional<Integer> pagesAmount) {
        try {
            return new ResponseEntity<>(employerInitializer.refreshData(vacancyName, location, pagesAmount), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, CONTROLLER_PATH + "/refresh", vacancyName, location);
        }
    }
}
