package com.bereznev.vacancies.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.vacancies.entity.Vacancy;
import com.bereznev.vacancies.model.Response;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public List<Vacancy> getVacanciesByName(String vacancyName){
        try {
            URL url = new URL("https://api.hh.ru/vacancies?text=" + vacancyName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.error("Request sending error. Response status: " + responseCode);
                return Collections.emptyList();
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }

            bufferedReader.close();
            connection.disconnect();
            return convertJsonVacanciesToList(response.toString());
        } catch (IOException e) {
            log.error("Request sending error: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
