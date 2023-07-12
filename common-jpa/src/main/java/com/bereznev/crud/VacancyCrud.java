package com.bereznev.crud;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Vacancy;

import java.util.List;

public interface VacancyCrud {

    public List<Vacancy> getAll();
    public List<Vacancy> getAllByName(String vacancyName);
    public List<Vacancy> getAllByLocation(String location);
    public List<Vacancy> getAllByNameAndLocation(String vacancyName, String location);
    public Vacancy getById(long vacancyId);

    public Vacancy save(Vacancy vacancy);
    public void saveAll(List<Vacancy> vacancies);

    public void deleteAll();
    public void delete(long id);
    public long countDatabaseLinesAmount();
}
