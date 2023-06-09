package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.vacancies.SalaryDTO;
import com.bereznev.model.Vacancy;
import com.bereznev.mapper.VacanciesMapper;
import com.bereznev.service.VacancyService;
import com.bereznev.utils.CurrencyConverter;
import com.bereznev.utils.HttpUtils;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        if (vacancy.getSalary().getFrom() == null) {
            vacancy.getSalary().setFrom(BigDecimal.ZERO);
        }
        if (vacancy.getSalary().getTo() == null) {
            vacancy.getSalary().setTo(BigDecimal.ZERO);
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

    @Override
    public SalaryDTO calculateMinMaxAvgSalary(String vacancyName) {
        BigDecimal lowestLimit = new BigDecimal(Integer.MAX_VALUE);
        BigDecimal highestLimit = new BigDecimal(Integer.MIN_VALUE);
        BigDecimal middlePriceSum = BigDecimal.ZERO;
        Vacancy lowestSalaryVacancy = new Vacancy();
        Vacancy highestSalaryVacancy = new Vacancy();
        List<Vacancy> vacancies = getVacanciesByName(vacancyName);
        for (Vacancy vacancy : vacancies) {
            if (!vacancy.getSalary().getCurrency().equals("RUR")) {
                if (vacancy.getSalary().getCurrency().equals("BYR")) {
                    vacancy.getSalary().setCurrency("BYN");
                }
                CurrencyConverter.convertCurrency(vacancy);
            }
            BigDecimal startPrice = vacancy.getSalary().getFrom();
            BigDecimal finishPrice = vacancy.getSalary().getTo();
            BigDecimal middlePrice = startPrice.add(finishPrice).divide(BigDecimal.valueOf(2));

            if (lowestLimit.compareTo(startPrice) > 0) {
                lowestLimit = startPrice;
                lowestSalaryVacancy = vacancy;
            }
            if (highestLimit.compareTo(finishPrice) < 0) {
                highestLimit = finishPrice;
                highestSalaryVacancy = vacancy;
            }
            middlePriceSum = middlePriceSum.add(middlePrice);
        }
        return new SalaryDTO("RUR", lowestLimit, lowestSalaryVacancy, highestLimit, highestSalaryVacancy, middlePriceSum.divide(BigDecimal.valueOf(vacancies.size())));
    }

    // TODO
    @Override
    public SalaryDTO calculateMinMaxAvgSalaryByArea(String vacancyName, String location) {
        BigDecimal lowestLimit = new BigDecimal(Integer.MAX_VALUE);
        BigDecimal highestLimit = new BigDecimal(Integer.MIN_VALUE);
        BigDecimal middlePriceSum = BigDecimal.ZERO;
        Vacancy lowestSalaryVacancy = new Vacancy();
        Vacancy highestSalaryVacancy = new Vacancy();
        List<Vacancy> vacancies = getVacanciesByName(vacancyName);
        int approvedVacanciesNumber = 0;
        for (Vacancy vacancy : vacancies) {
            if (!vacancy.getLocation().equals(location)) {
                continue;
            }
            approvedVacanciesNumber++;
            if (!vacancy.getSalary().getCurrency().equals("RUR")) {
                if (vacancy.getSalary().getCurrency().equals("BYR")) {
                    vacancy.getSalary().setCurrency("BYN");
                }
                CurrencyConverter.convertCurrency(vacancy);
            }
            BigDecimal startPrice = vacancy.getSalary().getFrom();
            BigDecimal finishPrice = vacancy.getSalary().getTo();
            BigDecimal middlePrice = startPrice.add(finishPrice).divide(BigDecimal.valueOf(2));

            if (lowestLimit.compareTo(startPrice) > 0) {
                lowestLimit = startPrice;
                lowestSalaryVacancy = vacancy;
            }
            if (highestLimit.compareTo(finishPrice) < 0) {
                highestLimit = finishPrice;
                highestSalaryVacancy = vacancy;
            }
            middlePriceSum = middlePriceSum.add(middlePrice);
        }
        BigDecimal avgValue;
        if (approvedVacanciesNumber == 0) {
            avgValue = BigDecimal.ZERO;
        } else {
            avgValue = middlePriceSum.divide(BigDecimal.valueOf(approvedVacanciesNumber));
        }
        return new SalaryDTO("RUR", lowestLimit, lowestSalaryVacancy, highestLimit, highestSalaryVacancy, avgValue);
    }
}
