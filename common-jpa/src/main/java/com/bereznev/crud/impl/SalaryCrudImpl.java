package com.bereznev.crud.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Salary;
import com.bereznev.repository.SalaryRepository;
import com.bereznev.crud.SalaryCrud;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
public class SalaryCrudImpl implements SalaryCrud {
    private final SalaryRepository salaryRepository;

    @Autowired
    public SalaryCrudImpl(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    @Override
    public void deleteAll() {
        try {
            salaryRepository.deleteAll();
            log.debug("All salaries data deleted");
        } catch (Exception e) {
            log.error("Error deleting salaries data: " + e.getMessage());
        }
    }

    @Override
    public Salary save(Salary salary) {
        return salaryRepository.save(salary);
    }

    @Override
    public long countDatabaseLinesAmount() {
        return salaryRepository.count();
    }

    @Override
    public void saveAll(List<Salary> salaries) {
        salaryRepository.saveAll(salaries);
    }
}
