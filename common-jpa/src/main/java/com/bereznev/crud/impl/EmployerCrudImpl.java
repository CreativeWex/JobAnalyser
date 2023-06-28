package com.bereznev.crud.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.crud.EmployerCrud;
import com.bereznev.entity.Employer;
import com.bereznev.exceptions.logic.ResourceNotFoundException;
import com.bereznev.repository.EmployerRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
public class EmployerCrudImpl implements EmployerCrud {
    private final EmployerRepository employerRepository;

    @Autowired
    public EmployerCrudImpl(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    @Override
    public void deleteAll() {
        try {
            employerRepository.deleteAll();
            log.debug("All employers data deleted");
        } catch (Exception e) {
            log.error("Error deleting employers data: " + e.getMessage());
        }
    }

    @Override
    public long countDatabaseLinesAmount() {
        return employerRepository.count();
    }

    public Employer getById(long employerId) {
        return employerRepository.findById(employerId).orElseThrow(() -> new ResourceNotFoundException("Employer", "Id", employerId));
    }

    @Override
    public Employer save(Employer employer) {
        return employerRepository.save(employer);
    }

    @Override
    public void saveAll(List<Employer> employers) {
        employerRepository.saveAll(employers);
    }

    @Override
    public List<Employer> getAll() {
        return employerRepository.findAll();
    }

    @Override
    public List<Employer> getAllFilteredByVacancyName(String vacancyName) {
        return employerRepository.getAllByVacancyName(vacancyName.substring(1));
    }

    @Override
    public List<Employer> getAllFilteredByVacancyNameAndLocation(String vacancyName, String location) {
        return employerRepository.getAllByVacancyNameAndLocation(vacancyName.substring(1), location.substring(1));
    }

    @Override
    public List<Employer> getAllFilteredByLocation(String location) {
        return employerRepository.getAllByLocation(location.substring(1));
    }
}
