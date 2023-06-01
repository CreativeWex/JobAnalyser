package com.bereznev.vacancies.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.vacancies.entity.Employer;

import java.util.List;

public interface EmployerService {
    public List<Employer> getEmployersByVacancy(String vacancyName);
    public Employer getById(long employerId);
//    public List<Employer> getEmployersByRegion(String region);
//    public List<Employer> getEmployersWithOpenVacancies();
//    public List<String> getRegionsWithMostEmployerAmount();
//    public List<String> getRegionsWithMostOpenVacanciesAmount();
//    public Employer getEmployerByName();
}
