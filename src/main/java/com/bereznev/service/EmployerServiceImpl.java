package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.EmployerDTO;
import com.bereznev.utils.HttpUtils;
import com.bereznev.model.Employer;
import com.bereznev.model.Vacancy;
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

    private Employer convertJsonEmployerToList(String jsonResponse) {
        Gson gson = new Gson();
        Employer employer = gson.fromJson(jsonResponse, Employer.class);

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject areaObject = jsonObject.getJSONObject("area");
        String areaName = areaObject.getString("name");
        employer.setLocation(areaName);
        if (employer.getDescription() != null) {
            employer.setDescription(employer.getDescription().replaceAll("\\<.*?\\>", ""));
        }
        return employer;
    }

    @Override
    public Employer getById(long employerId) {
        String response = HttpUtils.sendHttpRequest(EMPLOYERS_API_URL + employerId,
                "EmployerServiceImpl (getById)");
        return convertJsonEmployerToList(response);
    }

    //TODO: начал работу над методом
    @Override
    public EmployerDTO getAll() {
        String response = HttpUtils.sendHttpRequest(EMPLOYERS_API_URL,
                "EmployerServiceImpl (getAll())");
//        List<Employer> employers = convertJsonEmployerToList(response);

        return null;
    }

    @Override
    public EmployerDTO getEmployersByVacancy(String vacancyName) {
        List<Vacancy> vacancies = vacancyService.getVacanciesByName(vacancyName);
        Set<Employer> employers = new HashSet<>();
        for (Vacancy vacancy : vacancies) {
            if (vacancy.getEmployer().getId() == 0) {
                continue;
            }
            employers.add(getById(vacancy.getEmployer().getId()));
        }
        return new EmployerDTO(vacancyName, employers.size(), employers);
    }

    @Override
    public EmployerDTO getEmployersByVacancy(String vacancyName, String location) {
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
        return new EmployerDTO(vacancyName, employers.size(), employers);
    }
}
