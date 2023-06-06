package com.bereznev.vacancies.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.vacancies.entity.Employer;
import com.bereznev.vacancies.entity.Vacancy;
import com.bereznev.vacancies.model.json_response.EmployerResponse;
import com.bereznev.vacancies.utils.HttpUtils;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j
@Service
public class EmployerServiceImpl implements EmployerService {
    private final VacancyService vacancyService;
    private static final String EMPLOYERS_API_URL = "https://api.hh.ru/employers/";

    @Autowired
    public EmployerServiceImpl(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    private Employer convertJsonEmployersToList(String jsonResponse) {
        Gson gson = new Gson();
        Employer employer = gson.fromJson(jsonResponse, Employer.class);

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject areaObject = jsonObject.getJSONObject("area");
        String areaName = areaObject.getString("name");
        employer.setLocation(areaName);
        employer.setDescription(employer.getDescription().replaceAll("\\<.*?\\>", ""));

        return employer;
    }

    @Override
    public Employer getById(long employerId) {
        String response = HttpUtils.sendHttpRequest(EMPLOYERS_API_URL + employerId,
                "EmployerServiceImpl (getById)");
        return convertJsonEmployersToList(response);
    }

    @Override
    public EmployerResponse getEmployersByVacancy(String vacancyName) {
        List<Vacancy> vacancies = vacancyService.getVacanciesByName(vacancyName);
        Set<Employer> employers = new HashSet<>();
        for (Vacancy vacancy : vacancies) {
            if (vacancy.getEmployer().getId() == 0) {
                continue;
            }
            employers.add(getById(vacancy.getEmployer().getId()));
        }
        return new EmployerResponse(vacancyName, employers.size(), employers);
    }

    @Override
    public EmployerResponse getEmployersByVacancy(String vacancyName, String location) {
        List<Vacancy> vacancies = vacancyService.getVacanciesByName(vacancyName);
        Set<Employer> employers = new HashSet<>();
        for (Vacancy vacancy : vacancies) {
            if (vacancy.getEmployer().getId() == 0) {
                continue;
            }
            Employer employer = getById(vacancy.getEmployer().getId());
            if (!employer.getLocation().equalsIgnoreCase(location)) {
                continue;
            }
            employers.add(employer);
        }
        return new EmployerResponse(vacancyName, employers.size(), employers);
    }
}
