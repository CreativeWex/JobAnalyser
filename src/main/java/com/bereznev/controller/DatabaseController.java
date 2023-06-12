package com.bereznev.controller;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.service.DatabaseService;
import com.bereznev.service.EmployerService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j
@RestController
@RequestMapping("/api/v1/data")
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        super();
        this.databaseService = databaseService;
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refreshData() {
        databaseService.initData();
        return new ResponseEntity<>("Data refreshed!", HttpStatus.OK);
    }

    @GetMapping("/clear")
    public ResponseEntity<String> clearData() {
        return new ResponseEntity<>("All data deleted!", HttpStatus.OK);
    }

}
