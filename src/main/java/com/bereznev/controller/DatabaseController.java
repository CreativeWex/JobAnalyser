package com.bereznev.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.DataInitializerDTO;
import com.bereznev.service.DataInitializer;
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
@RequestMapping("/api/v1/data")
public class DatabaseController {

    private final DataInitializer dataInitializer;

    private static final String REFRESH_DESCRIPTION_MESSAGE = "Data refreshed successfully";
    private static final String CLEAR_DESCRIPTION_MESSAGE = "Data refreshed successfully";

    public DatabaseController(DataInitializer dataInitializer) {
        super();
        this.dataInitializer = dataInitializer;
    }

    @GetMapping("/refresh")
    public ResponseEntity<DataInitializerDTO> refreshData(@RequestParam(value = "name", required = false) Optional<String> vacancyName) {
        long startTime = System.currentTimeMillis();
        try {
            dataInitializer.initData(vacancyName);
            DataInitializerDTO dto = new DataInitializerDTO("success", REFRESH_DESCRIPTION_MESSAGE, LocalDateTime.now(),  System.currentTimeMillis() - startTime + " ms");
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            DataInitializerDTO dto = new DataInitializerDTO("error", e.getMessage(), LocalDateTime.now(), System.currentTimeMillis() - startTime + " ms");
            return new ResponseEntity<>(dto , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clear")
    public ResponseEntity<DataInitializerDTO> clearData() {
        long startTime = System.currentTimeMillis();
        try {
            dataInitializer.deleteAllData();
            DataInitializerDTO dto = new DataInitializerDTO("success", CLEAR_DESCRIPTION_MESSAGE, LocalDateTime.now(),  System.currentTimeMillis() - startTime + " ms");
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            DataInitializerDTO dto = new DataInitializerDTO("error", e.getMessage(), LocalDateTime.now(), System.currentTimeMillis() - startTime + " ms");
            return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
