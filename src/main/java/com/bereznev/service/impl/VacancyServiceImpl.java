package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.exception.AlreadyExistsException;
import com.bereznev.model.Salary;
import com.bereznev.model.Vacancy;
import com.bereznev.mapper.VacanciesMapper;
import com.bereznev.repository.VacancyRepository;
import com.bereznev.service.VacancyService;
import com.bereznev.utils.HttpUtils;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
public class VacancyServiceImpl implements VacancyService {

    private static final String VACANCY_API_URL = "https://api.hh.ru/vacancies";
    private static final String RESOURCE_NAME = "Vacancy";

    private final VacancyRepository vacancyRepository;

    @Autowired
    public VacancyServiceImpl(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    private Vacancy convertJsonVacancyToObject(String jsonResponse) {
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        Vacancy vacancy = gson.fromJson(jsonResponse, Vacancy.class);

        JSONObject locationJson = jsonObject.optJSONObject("area");
        if (locationJson != null) {
            vacancy.setLocation(locationJson.optString("name"));
        }
        JSONObject experienceJson = jsonObject.optJSONObject("experience");
        if (experienceJson != null) {
            vacancy.setExperienceAmount(experienceJson.optString("name"));
        }
        JSONObject scheduleJson = jsonObject.optJSONObject("schedule");
        if (scheduleJson != null) {
            vacancy.setWorkSchedule(scheduleJson.optString("name"));
        }
        JSONObject employmentJson = jsonObject.optJSONObject("employment");
        if (employmentJson != null) {
            vacancy.setWorkEmployment(employmentJson.optString("name"));
        }
        JSONObject salaryJson = jsonObject.optJSONObject("salary");
        Salary salary = new Salary(BigDecimal.ZERO, BigDecimal.ZERO, "RUR");
        if (salaryJson != null) {
            salary = new Salary(
                salaryJson.optBigDecimal("from", BigDecimal.ZERO),
                salaryJson.optBigDecimal("to", BigDecimal.ZERO),
                salaryJson.optString("currency", "RUR")
            );
        }
        vacancy.setSalary(salary);
        vacancy.setDescription(vacancy.getDescription()
                .replaceAll("\\<.*?\\>", "")
                .replace("&quot;", "\"")); //FIXME
        return vacancy;
    }
    @Override
    public Vacancy getById(long vacancyId) {
        String response = HttpUtils.sendHttpRequest(VACANCY_API_URL+ "/" + vacancyId,
                "VacancyServiceImpl (getById)");
        return convertJsonVacancyToObject(response);
    }

    @Override
    public void saveAll(List<Vacancy> vacancies) {
        vacancyRepository.saveAll(vacancies);
    }

    @Override
    public void deleteAll() {
        try {
            vacancyRepository.deleteAll();
            log.debug("All vacancies data deleted");
        } catch (Exception e) {
            log.error("Error deleting vacancies data: " + e.getMessage());
        }
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
//        if (vacancyRepository.findVacanciesByNameAndLocationAndEmployerId(vacancy.getName(), vacancy.getLocation(),
//                vacancy.getEmployer().getId()).isPresent()) {
//            throw new AlreadyExistsException(RESOURCE_NAME, "name / employer", vacancy.getName() + " / " + vacancy.getEmployer());
//        }
        return vacancyRepository.save(vacancy);
    }

    @Override
    public long countDatabaseLinesAmount() {
        return vacancyRepository.count();
    }

    private List<Vacancy> convertJsonVacanciesToList(String jsonResponse) {
        Gson gson = new Gson();
        List<Vacancy> incompleteInfoVacancies = gson.fromJson(jsonResponse, VacanciesMapper.class).getItems();
        List<Vacancy> filledInfoVacancies = new ArrayList<>();
        for (int i = 0; i < incompleteInfoVacancies.size(); i++) {
            filledInfoVacancies.add(getById(incompleteInfoVacancies.get(i).getId()));
        }
        return filledInfoVacancies;
    }

    @Override
    public List<Vacancy> getVacanciesByName(String vacancyName) {
        vacancyName = vacancyName.trim().replace(" ","+");
//        int pagesNumber = countPagesNumber(vacancyName);
//        List<Vacancy> vacancies = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            String response = HttpUtils.sendHttpRequest( VACANCY_API_URL + "?text=" + vacancyName + "&page=" + i,
//                    "VacancyServiceImpl (getVacanciesByName)");
//            vacancies.addAll(convertJsonVacanciesToList(response));
//
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return vacancies;

        String response = HttpUtils.sendHttpRequest( VACANCY_API_URL + "?text=" + vacancyName,
                "VacancyServiceImpl (getVacanciesByName)");
        return convertJsonVacanciesToList(response);
    }

}
