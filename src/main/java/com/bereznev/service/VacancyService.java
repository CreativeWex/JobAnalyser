package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.SkillsDto;
import com.bereznev.entity.Vacancy;

import java.util.List;
import java.util.Optional;

public interface VacancyService {

    public List<Vacancy> getAll();
    public List<Vacancy> getAllByName(String vacancyName);
    public List<Vacancy> getAllByNameAndLocation(String vacancyName, String location);

    public Vacancy getById(long vacancyId);

    public Vacancy save(Vacancy vacancy);
    public void saveAll(List<Vacancy> vacancies);

    public void deleteAll();

    public long countDatabaseLinesAmount();

    public SkillsDto getMostPopularSkills(Optional<String> vacancyName, Optional<String> location, Optional<Integer> amount);
}
