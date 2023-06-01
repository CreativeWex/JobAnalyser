package com.bereznev.vacancies.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.vacancies.entity.Vacancy;

import java.util.List;

public interface VacancyService {
    public List<Vacancy> getVacanciesByName(String vacancyName);
}
