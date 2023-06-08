package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.vacancies.SalaryDTO;
import com.bereznev.model.Salary;
import com.bereznev.model.Vacancy;
import com.bereznev.mapper.VacanciesMapper;
import com.bereznev.service.EmployerService;
import com.bereznev.service.VacancyService;
import com.bereznev.utils.HttpUtils;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j

@Service
public class VacancyServiceImpl implements VacancyService {
    private static final String VACANCY_API_URL = "https://api.hh.ru/vacancies";

    private Vacancy convertJsonVacancyToObject(String jsonResponse) {
        Gson gson = new Gson();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        Vacancy vacancy = gson.fromJson(jsonResponse, Vacancy.class);

        jsonObject.optInt("from");
        jsonObject.optInt("to");
        jsonObject.optString("currency");

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
        vacancy.setDescription(vacancy.getDescription().replaceAll("\\<.*?\\>", ""));
        return vacancy;
    }
    @Override
    public Vacancy getById(long vacancyId) {
        String response = HttpUtils.sendHttpRequest(VACANCY_API_URL+ "/" + vacancyId,
                "VacancyServiceImpl (getById)");
        return convertJsonVacancyToObject(response);
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

    private int countPagesNumber(String vacancyName) {
        String response = HttpUtils.sendHttpRequest( VACANCY_API_URL + "?text=" + vacancyName,
                "VacancyServiceImpl (countPagesNumber)");
        return new JSONObject(response).getInt("pages");
    }

    // TODO: количество страниц в цикле
    // TODO: не заполняются Employers
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

    private void convertCurrency(Vacancy vacancy) {
        // узнать, чему равно значние vacancy.getSalary().getCurrency(), например, оно может быть "BYR", "KZT", "USD" и прочие валюты;
        // double convertedStartPrice = переконвертировать vacancy.getSalary().getFrom() из представленной в поле currency валюты в ваплюту RUR;
        // double convertedFinishPrice = переконвертировать vacancy.getSalary().getTo() из представленной в поле currency валюты в ваплюту RUR;
//        vacancy.setSalary(new Salary(convertedStartPrice, convertedFinishPrice, "RUR"));
    }

    @Override
    public SalaryDTO calculateMinMaxAvgSalary(String vacancyName) {
        double lowestLimit = Double.MAX_VALUE;
        double highestLimit = Double.MIN_VALUE;
        double middlePriceSum = 0;
        Vacancy lowestSalaryVacancy = new Vacancy();
        Vacancy highestSalaryVacancy = new Vacancy();
        List<Vacancy> vacancies = getVacanciesByName(vacancyName);
        for (Vacancy vacancy : vacancies) {
            double startPrice = vacancy.getSalary().getFrom();
            double finishPrice = vacancy.getSalary().getTo();
            double middlePrice = (startPrice + finishPrice) / 2;
            if (lowestLimit > startPrice) {
                lowestLimit = startPrice;
                lowestSalaryVacancy = vacancy;
            }
            if (highestLimit < finishPrice) {
                highestLimit = finishPrice;
                highestSalaryVacancy = vacancy;
            }
            middlePriceSum += middlePrice;
        }
        return new SalaryDTO("RUR", lowestLimit, lowestSalaryVacancy, highestLimit, highestSalaryVacancy, middlePriceSum / vacancies.size());
    }
}
