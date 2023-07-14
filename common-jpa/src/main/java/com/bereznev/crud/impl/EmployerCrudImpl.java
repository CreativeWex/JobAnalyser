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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
public class EmployerCrudImpl implements EmployerCrud {
    private final EmployerRepository employerRepository;
    private static final String RESOURCE_NAME = "Employer";

    @Autowired
    public EmployerCrudImpl(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    @Override
    @CacheEvict(value = "employers")
    public void deleteAll() {
        try {
            employerRepository.deleteAll();
            log.debug("All employers data deleted");
        } catch (Exception e) {
            log.error("Error deleting employers data: " + e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "employers", key = "#id")
    public void delete(long id) {
        employerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Id", id));
        employerRepository.deleteById(id);
        log.debug("deleted, id: " + id);
    }
    @Override
    public long countDatabaseLinesAmount() {
        return employerRepository.count();
    }

    @Override
    @Cacheable(value = "employers", key = "#employerId")
    public Employer getById(long employerId) {
        return employerRepository.findById(employerId).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Id", employerId));
    }

    @Override
    @CachePut(value = "employers", key = "#result.id")
    public Employer save(Employer employer) {
        return employerRepository.save(employer);
    }

    @Override
    @CachePut(value = "employers", key = "#result.id")
    public void saveAll(List<Employer> employers) {
        employerRepository.saveAll(employers);
    }

    @Override
    @Cacheable(value = "employers")
    public List<Employer> getAll() {
        return employerRepository.findAll();
    }

    @Override
    @Cacheable(value = "employersFilteredByVacancyName")
    public List<Employer> getAllFilteredByVacancyName(String vacancyName) {
        return employerRepository.getAllByVacancyName(vacancyName.substring(1));
    }

    @Override
    @Cacheable(value = "employersFilteredByVacancyNameAndLocation")
    public List<Employer> getAllFilteredByVacancyNameAndLocation(String vacancyName, String location) {
        return employerRepository.getAllByVacancyNameAndLocation(vacancyName.substring(1), location.substring(1));
    }

    @Override
    @Cacheable(value = "employersFilteredByLocation")
    public List<Employer> getAllFilteredByLocation(String location) {
        return employerRepository.getAllByLocation(location.substring(1));
    }
}
