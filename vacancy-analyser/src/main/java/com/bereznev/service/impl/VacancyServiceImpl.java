package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.crud.VacancyCrud;
import com.bereznev.dto.VacancyDto;
import com.bereznev.entity.Vacancy;
import com.bereznev.repository.VacancyRepository;
import com.bereznev.service.VacancyService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Log4j
@Service
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;
    private final VacancyCrud vacancyCrud;

    @Autowired
    public VacancyServiceImpl(VacancyRepository vacancyRepository, VacancyCrud vacancyCrud) {
        this.vacancyRepository = vacancyRepository;
        this.vacancyCrud = vacancyCrud;
    }

    @Override
    public VacancyDto getAll(Optional<String> vacancyName, Optional<String> location) {
        VacancyDto vacancyDto = new VacancyDto();
        final long startTime = System.currentTimeMillis();

        if (vacancyName.isPresent()) {
            vacancyDto.setNameFilter(vacancyName.get());
        }
        if (location.isPresent()) {
            vacancyDto.setLocationFilter(location.get());
        }

        List<Vacancy> vacancies;
        if (vacancyName.isPresent() && location.isPresent()) {
            vacancies = vacancyCrud.getAllByNameAndLocation(vacancyName.get(), location.get());
        } else if (vacancyName.isPresent()) {
            vacancies = vacancyCrud.getAllByName(vacancyName.get());
        } else if (location.isPresent()) {
            vacancies = vacancyCrud.getAllByLocation(location.get());
        } else {
            vacancies = vacancyCrud.getAll();
        }

        vacancyDto.setVacancies(vacancies);
        vacancyDto.setVacanciesNumber(vacancyDto.getVacancies().size());
        vacancyDto.setTimeSpent(System.currentTimeMillis() - startTime + " ms");
        return vacancyDto;
    }
}
