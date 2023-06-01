package com.bereznev.vacancies.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.vacancies.entity.Employer;
import com.bereznev.vacancies.entity.Vacancy;
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

    //TODO: переиспользование кода
    //TODO: добавить area
    @Override
    public Employer getById(long employerId) {
        try {
            URL url = new URL("https://api.hh.ru/employers/" + employerId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.error("Request sending error. Response status: " + responseCode);
                return new Employer();
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }

            bufferedReader.close();
            connection.disconnect();
            return convertJsonEmployersToList(response.toString());
        } catch (IOException e) {
            log.error("Request sending error: " + e.getMessage());
            return new Employer();
        }
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
