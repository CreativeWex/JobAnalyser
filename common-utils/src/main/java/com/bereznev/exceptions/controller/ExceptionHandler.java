package com.bereznev.exceptions.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.exceptions.dto.ErrorDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@Log4j
@RestController
@RequestMapping("/api/v1/vacancies")
public class ExceptionHandler {

    private ExceptionHandler() {
    }

    public static ResponseEntity<ErrorDTO> handleException(Exception e, String controllerPath, Optional<String> vacancyName, Optional<String> location) {
        ErrorDTO dto = new ErrorDTO();
        dto.setExceptionMessage(e.getMessage());
        dto.setLocalDateTime(LocalDateTime.now());
        dto.setEndpoint(controllerPath);
        if (vacancyName.isPresent()) {
            dto.setNameFilter(vacancyName.get());
        }
        if (location.isPresent()) {
            dto.setLocationFilter(location.get());
        }
        log.error(dto);
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<?> handleException(Exception e, String controllerPath) {
        ErrorDTO dto = new ErrorDTO();
        dto.setExceptionMessage(e.getMessage());
        dto.setLocalDateTime(LocalDateTime.now());
        dto.setEndpoint(controllerPath);
        log.error(dto);
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
