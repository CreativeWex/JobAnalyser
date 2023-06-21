package com.bereznev.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.ErrorDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Log4j
@RestController
@RequestMapping("/api/v1/vacancies")
public class ExceptionHandler {

    private ExceptionHandler() {
    }

    public static ResponseEntity<ErrorDTO> handleException(Exception e, String endpoint) {
        ErrorDTO dto = new ErrorDTO();
        dto.setExceptionMessage(e.getMessage());
        dto.setLocalDateTime(LocalDateTime.now());
        dto.setEndpoint(endpoint);
        log.error(dto);
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
