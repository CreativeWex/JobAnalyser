package com.bereznev.vacancies.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.vacancies.entity.Employer;
import com.bereznev.vacancies.entity.Vacancy;
import com.bereznev.vacancies.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
public class EmployerServiceImpl implements EmployerService{
    private final VacancyService vacancyService;

    @Autowired
    public EmployerServiceImpl(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    private Employer convertJsonEmployersToList(String jsonResponse) {
        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, Employer.class);
    }

    //TODO: добавить area
    @Override
    public Employer getById(long employerId) {
        String response = HttpUtils.sendHttpRequest("https://api.hh.ru/employers/" + employerId, "EmployerServiceImpl (getById)");
        return convertJsonEmployersToList(response);
    }

    @Override
    public List<Employer> getEmployersByVacancy(String vacancyName) {
        List<Vacancy> vacancies = vacancyService.getVacanciesByName(vacancyName);
        List<Employer> employers = new ArrayList<>();
        for (Vacancy vacancy : vacancies) {
            employers.add(getById(vacancy.getEmployer().getId()));
        }
        return employers;
    }
}
