package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.crud.EmployerCrud;
import com.bereznev.dto.EmployerDto;
import com.bereznev.service.EmployerService;
import com.bereznev.entity.Employer;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Log4j
@Service
public class EmployerServiceImpl implements EmployerService {
    private final EmployerCrud employerCrud;

    @Autowired
    public EmployerServiceImpl(EmployerCrud employerCrud) {
        this.employerCrud = employerCrud;
    }

    @Override
    public EmployerDto getAll(Optional<String> vacancyName, Optional<String> location) {
        EmployerDto employerDto = new EmployerDto();
        final long startTime = System.currentTimeMillis();

        if (vacancyName.isPresent()) {
            employerDto.setNameFilter(vacancyName.get());
        }
        if (location.isPresent()) {
            employerDto.setLocationFilter(location.get());
        }

        List<Employer> employers;
        if (vacancyName.isPresent() && location.isPresent()) {
            employers = employerCrud.getAllFilteredByVacancyNameAndLocation(vacancyName.get(), location.get());
        } else if (vacancyName.isPresent()) {
            employers = employerCrud.getAllFilteredByVacancyName(vacancyName.get());
        } else if (location.isPresent()) {
            employers = employerCrud.getAllFilteredByLocation(location.get());
        } else {
            employers = employerCrud.getAll();
        }
        employerDto.setEmployers(employers);
        employerDto.setTimeSpent(System.currentTimeMillis() - startTime + "ms");
        employerDto.setEmployersNumber(employers.size());
        return employerDto;
    }

    @Override //FIXME
    public String compareTo(long firstId, long secondId) {
        Employer firstEmployer = employerCrud.getById(firstId);
        Employer secondEmployer = employerCrud.getById(secondId);
        log.debug(String.format("Comparing Employers: %s and %s", firstEmployer, secondEmployer));
        return "compared";
    }
}
