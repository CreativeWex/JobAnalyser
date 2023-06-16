package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Vacancy;

import java.util.List;

public interface VacancyService {

    public List<Vacancy> getVacanciesByName(String vacancyName);

    public Vacancy getById(long vacancyId);

    public void saveAll(List<Vacancy> vacancies);

    public void deleteAll();

    public Vacancy save(Vacancy vacancy);
    public long countDatabaseLinesAmount();
}
