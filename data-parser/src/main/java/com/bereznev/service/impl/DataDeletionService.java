package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.crud.EmployerCrud;
import com.bereznev.crud.SalaryCrud;
import com.bereznev.crud.SkillCrud;
import com.bereznev.crud.VacancyCrud;
import com.bereznev.exceptions.logic.DataInitialisationException;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j
@Service
public class DataDeletionService {
    private final EmployerCrud employerCrud;
    private final VacancyCrud vacancyCrud;
    private final SalaryCrud salaryCrud;
    private final SkillCrud skillCrud;

    @Autowired
    public DataDeletionService(EmployerCrud employerCrud, VacancyCrud vacancyCrud, SalaryCrud salaryCrud, SkillCrud skillCrud) {
        this.employerCrud = employerCrud;
        this.vacancyCrud = vacancyCrud;
        this.salaryCrud = salaryCrud;
        this.skillCrud = skillCrud;
    }

    @Transactional
    public void deleteAllData() {
        log.debug("deleteAllData invoked");
        long startTime = System.currentTimeMillis();
        try {
            if (skillCrud.countDatabaseLinesAmount() > 0) {
                skillCrud.deleteAll();
            }
            if (vacancyCrud.countDatabaseLinesAmount() > 0) {
                vacancyCrud.deleteAll();
            }
            if (salaryCrud.countDatabaseLinesAmount() > 0) {
                salaryCrud.deleteAll();
            }
            if (employerCrud.countDatabaseLinesAmount() > 0) {
                employerCrud.deleteAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataInitialisationException("deleteAllData", e.getMessage());
        }
        log.debug(String.format("All data removed successfully, time: %d ms", System.currentTimeMillis() - startTime));
    }
}
