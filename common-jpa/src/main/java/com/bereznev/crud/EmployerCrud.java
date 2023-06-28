package com.bereznev.crud;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Employer;

import java.util.List;

public interface EmployerCrud {
    public List<Employer> getAll();
    public List<Employer> getAllFilteredByVacancyName(String vacancyName);
    public List<Employer> getAllFilteredByVacancyNameAndLocation(String vacancyName, String location);

    public List<Employer> getAllFilteredByLocation(String location);

    public Employer getById(long employerId);
    public Employer save(Employer employer);
    public void saveAll(List<Employer> employers);
    public void deleteAll();
    public long countDatabaseLinesAmount();
}
