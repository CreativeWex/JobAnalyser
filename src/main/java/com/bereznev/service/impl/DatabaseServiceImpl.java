package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.model.Employer;
import com.bereznev.service.DatabaseService;
import com.bereznev.service.EmployerService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Log4j
@Service
public class DatabaseServiceImpl implements DatabaseService {

    private final EmployerService employerService;

    @Autowired
    public DatabaseServiceImpl(EmployerService employerService) {
        this.employerService = employerService;
    }

    @Override
    public void initData() {
        log.debug("initData");
        Set<Employer> employers = employerService.getAll().getEmployers();
        log.debug(employers);
        for (Employer employer : employers) {
            employerService.save(employer);
        }
    }

    @Override
    public void deleteAllData() {

    }
}
