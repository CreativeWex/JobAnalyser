package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Employer;
import com.bereznev.entity.Vacancy;
import com.google.gson.JsonObject;
import org.json.JSONObject;

public interface VacancyInitService {
    public void fillVacanciesForEmployer(Employer employer);
    public Vacancy fillVacancyFields(Vacancy vacancy);
    public void fillKeySkillsForVacancy(JsonObject jsonObject, Vacancy vacancy);
    public void fillSalaryForVacancy(JSONObject jsonObject, Vacancy vacancy);
    public void deleteVacanciesForEmployers(Employer employer);
}
