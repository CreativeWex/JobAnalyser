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

import java.util.Optional;

@Log4j
@RestController
@RequestMapping("/api/v1/data")
public class DatabaseController {

    private final DataInitializer dataInitializer;

    public DatabaseController(DataInitializer dataInitializer) {
        super();
        this.dataInitializer = dataInitializer;
    }

    @GetMapping("/refresh")
    public ResponseEntity<DataInitializerDTO> refreshData(@RequestParam(value = "name", required = false) Optional<String> vacancyName) {
        try {
            long startTime = System.currentTimeMillis();
            dataInitializer.initData(vacancyName);
            return new ResponseEntity<>(new DataInitializerDTO("success", System.currentTimeMillis() - startTime + " ms"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new DataInitializerDTO(e.getMessage(), null), HttpStatus.OK);
        }
    }

    @GetMapping("/clear")
    public ResponseEntity<DataInitializerDTO> clearData() {
        try {
            long startTime = System.currentTimeMillis();
            dataInitializer.deleteAllData();
            return new ResponseEntity<>(new DataInitializerDTO("success", System.currentTimeMillis() - startTime + " ms"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new DataInitializerDTO(e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
