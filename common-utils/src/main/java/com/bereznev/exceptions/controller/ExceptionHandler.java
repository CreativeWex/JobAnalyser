package com.bereznev.exceptions.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.exceptions.dto.ErrorDTO;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@Log4j
@Data

@RestController
@RequestMapping("/api/v1/vacancies")
public class ExceptionHandler {

    public static String exceptionReason; // value inputs from data-parser - HttpUtils if http request exception will be thrown

    private ExceptionHandler() {
    }

    public static ResponseEntity<ErrorDTO> handleException(Exception e, String controllerPath, Optional<String> vacancyName, Optional<String> location) {
        ErrorDTO dto = new ErrorDTO();
        if (exceptionReason == null) {
            dto.setExceptionMessage(e.getMessage());
        } else {
            dto.setExceptionMessage(exceptionReason);
        }
        dto.setLocalDateTime(LocalDateTime.now());
        dto.setEndpoint(controllerPath);

        vacancyName.ifPresent(dto::setNameFilter);
        location.ifPresent(dto::setLocationFilter);
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
