package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.model.Employer;
import com.bereznev.dto.EmployerDTO;

public interface EmployerService {

    public EmployerDTO getAll();
    public EmployerDTO getEmployersByVacancy(String vacancyName);
    public EmployerDTO getEmployersByVacancy(String vacancyName, String location);
    public Employer getById(long employerId);
//    public List<Employer> getEmployersByRegion(String region);
//    public List<Employer> getEmployersWithOpenVacancies();
//    public List<String> getRegionsWithMostEmployerAmount();
//    public List<String> getRegionsWithMostOpenVacanciesAmount();
//    public Employer getEmployerByName();
}
