package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.exception.NotFoundException;
import com.bereznev.repository.EmployerRepository;
import com.bereznev.service.EmployerService;
import com.bereznev.entity.Employer;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
public class EmployerServiceImpl implements EmployerService {

    private static final String EMPLOYERS_API_URL = "https://api.hh.ru/employers";

    private final EmployerRepository employerRepository;

    @Autowired
    public EmployerServiceImpl(EmployerRepository employerRepository) {
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

    @Override //todo рафакторинг
    public Employer getById(long employerId) {
        if (employerRepository.findById(employerId).isPresent()) {
            return employerRepository.findById(employerId).get();
        } else {
            throw new NotFoundException("Employer", "id", employerId);
        }
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
    public List<Employer> getAllFilteredByVacancy(String vacancyName) {
        return employerRepository.getAllByVacancyName(vacancyName);
    }

    @Override
    public List<Employer> getAllFilteredByVacancyAndLocation(String vacancyName, String location) {
        return employerRepository.getAllByVacancyNameAndLocation(vacancyName, location);
    }
}
