package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.model.Employer;
import com.bereznev.dto.EmployerDTO;
import com.bereznev.model.Vacancy;

import java.util.List;

public interface EmployerService {

    public EmployerDTO getAll();
    public EmployerDTO getAllFilteredByVacancy(String vacancyName);
    public EmployerDTO getAllFilteredByVacancyAndLocation(String vacancyName, String location);
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
