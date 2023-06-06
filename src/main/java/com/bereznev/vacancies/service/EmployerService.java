package com.bereznev.vacancies.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.vacancies.entity.Employer;
import com.bereznev.vacancies.model.json_response.EmployerResponse;

public interface EmployerService {
    public EmployerResponse getEmployersByVacancy(String vacancyName);
    public EmployerResponse getEmployersByVacancy(String vacancyName, String location);
    public Employer getById(long employerId);
//    public List<Employer> getEmployersByRegion(String region);
//    public List<Employer> getEmployersWithOpenVacancies();
//    public List<String> getRegionsWithMostEmployerAmount();
//    public List<String> getRegionsWithMostOpenVacanciesAmount();
//    public Employer getEmployerByName();
}
