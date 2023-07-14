package com.bereznev.crud.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Salary;
import com.bereznev.exceptions.logic.ResourceNotFoundException;
import com.bereznev.repository.SalaryRepository;
import com.bereznev.crud.SalaryCrud;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
public class SalaryCrudImpl implements SalaryCrud {
    private final SalaryRepository salaryRepository;
    private static final String RESOURCE_NAME = "Salary";

    @Autowired
    public SalaryCrudImpl(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    @Override
    @CacheEvict(value = "salaries")
    public void deleteAll() {
        try {
            salaryRepository.deleteAll();
            log.debug("All salaries data deleted");
        } catch (Exception e) {
            log.error("Error deleting salaries data: " + e.getMessage());
        }
    }

    @Override
    @CachePut(value = "salaries", key = "#result.id")
    public Salary save(Salary salary) {
        return salaryRepository.save(salary);
    }

    @Override
    public long countDatabaseLinesAmount() {
        return salaryRepository.count();
    }

    @Override
    @CachePut(value = "employers", key = "#result.id")
    public void saveAll(List<Salary> salaries) {
        salaryRepository.saveAll(salaries);
    }

    @Override
    @CacheEvict(value = "employers", key = "#id")
    public void delete(long id) {
        salaryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Id", id));
        salaryRepository.deleteById(id);
        log.debug("deleted, id: " + id);
    }
}
