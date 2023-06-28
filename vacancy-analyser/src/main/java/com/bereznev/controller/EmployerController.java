package com.bereznev.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.crud.EmployerCrud;
import com.bereznev.exceptions.controller.ExceptionHandler;
import com.bereznev.service.EmployerService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Log4j
@RestController
@RequestMapping("/api/v1/employers")
public class EmployerController {
    private static final String CONTROLLER_PATH = "/api/v1/employers";

    private final EmployerService employerService;
    private final EmployerCrud employerCrud;

    public EmployerController(EmployerService employerService, EmployerCrud employerCrud) {
        super();
        this.employerService = employerService;
        this.employerCrud = employerCrud;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(value = "vacancy", required = false) Optional<String> vacancyName,
            @RequestParam(value = "location", required = false) Optional<String> location) {
        try {
            return new ResponseEntity<>(employerService.getAll(vacancyName, location), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, CONTROLLER_PATH);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(value = "id") long id) {
        try {
            return new ResponseEntity<>(employerCrud.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, CONTROLLER_PATH + "/" + id);
        }
    }

    @GetMapping("/{id}/compare_to")
    public ResponseEntity<?> compareTo(@PathVariable(value = "id") long firstId, @RequestParam(value = "id") long secondId) {
        try {
            return new ResponseEntity<>(employerService.compareTo(firstId, secondId), HttpStatus.OK);
        } catch (Exception e) {
            return ExceptionHandler.handleException(e, CONTROLLER_PATH + String.format("/%s/compare_to=%s", firstId, secondId));
        }
    }
}
