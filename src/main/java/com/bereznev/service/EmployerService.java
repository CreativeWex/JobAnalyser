package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Employer;

import java.util.List;

public interface EmployerService {

    public List<Employer> getAll();
    public List<Employer> getAllFilteredByVacancy(String vacancyName);
    public List<Employer> getAllFilteredByVacancyAndLocation(String vacancyName, String location);
    public Employer getById(long employerId);
    public Employer save(Employer employer);
    public void saveAll(List<Employer> employers);
    public void deleteAll();
    public long countDatabaseLinesAmount();

//    public List<Employer> getEmployersByRegion(String region);
//    public List<Employer> getEmployersWithOpenVacancies();
//    public List<String> getRegionsWithMostEmployerAmount();
//    public List<String> getRegionsWithMostOpenVacanciesAmount();
//    public Employer getEmployerByName();
}
