package com.bereznev.vacancies.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.vacancies.entity.Vacancy;
import com.bereznev.vacancies.model.Response;
import com.bereznev.vacancies.utils.HttpUtils;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Log4j

@Service
public class VacancyServiceImpl implements VacancyService {

    private List<Vacancy> convertJsonVacanciesToList(String jsonResponse) {
        Gson gson = new Gson();
        Response response = gson.fromJson(jsonResponse, Response.class);
        return response.getItems();
    }

    // Todo обрабатывается только 1 страница
    @Override
    public List<Vacancy> getVacanciesByName(String vacancyName) {
        String response = HttpUtils.sendHttpRequest("https://api.hh.ru/vacancies?text=" + vacancyName, "VacancyServiceImpl (getVacanciesByName)");
        return convertJsonVacanciesToList(response);
    }
}
