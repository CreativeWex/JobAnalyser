package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.InputValueFormatter;
import com.bereznev.crud.EmployerCrud;
import com.bereznev.dao.EmployerDao;
import com.bereznev.dto.DataParserDTO;
import com.bereznev.exceptions.logic.DataInitialisationException;
import com.bereznev.service.EmployerInitializer;
import com.bereznev.service.VacanciesInitializer;
import com.bereznev.mapper.EmployersMapper;
import com.bereznev.entity.Employer;
import com.bereznev.utils.HttpUtils;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j
@Service
@Transactional
public class EmployersInitializerImpl implements EmployerInitializer {
    private static final String EMPLOYERS_HH_API_URL = "https://api.hh.ru/employers";

    private final EmployerCrud employerCrud;
    private final EmployerDao employerDao;
    private final VacanciesInitializer vacanciesInitializer;
    private final LocationIdCalculator locationIdCalculator;

    private long pagesNumber;

    @Autowired
    public EmployersInitializerImpl(EmployerCrud employerCrud, EmployerDao employerDao, VacanciesInitializer vacanciesInitializer, LocationIdCalculator locationIdCalculator) {
        this.employerCrud = employerCrud;
        this.employerDao = employerDao;
        this.vacanciesInitializer = vacanciesInitializer;
        this.locationIdCalculator = locationIdCalculator;
    }

    public int countJsonResponsePages(String url) {
        String response = HttpUtils.sendHttpRequest( url, "DataInitializerImpl (countJSONResponsePages)");
        return new JSONObject(response).getInt("pages");
    }

    public List<Employer> parseEmployersFromJsonResponse(String jsonResponse) {
        Gson gson = new Gson();
        EmployersMapper employersMapper = gson.fromJson(jsonResponse, EmployersMapper.class);
        List<Employer> employers = employersMapper.getItems();
        for (Employer employer : employers) {
            String getByIdResponse = HttpUtils.sendHttpRequest(EMPLOYERS_HH_API_URL + "/" + employer.getId(),
                    "DataInitializerImpl (convertJsonEmployersToList)");
            JSONObject jsonObject = new JSONObject(getByIdResponse);
            JSONObject areaObject = jsonObject.getJSONObject("area");
            String areaName = areaObject.getString("name");
            employer.setLocation(areaName);

            String description =  jsonObject.optString("description");
            if (description != null) {
                employer.setDescription(description.replaceAll("\\<.*?\\>", ""));
            }
            employer.setOpenVacanciesAmount(jsonObject.optInt("open_vacancies"));
            employerCrud.save(employer);
            vacanciesInitializer.fillVacanciesForEmployer(employer);
        }
        return employers;
    }

    public List<Employer> getAllEmployers(Optional<Integer> userPagesAmount) {
        long currentRequestPagesAmount;
        log.debug(String.format("getAllEmployersData(%s) invoked", userPagesAmount));
        long maxPagesAmount = countJsonResponsePages(EMPLOYERS_HH_API_URL);
        if (userPagesAmount.isPresent() && userPagesAmount.get() <= maxPagesAmount) {
            currentRequestPagesAmount = userPagesAmount.get();
        } else {
            currentRequestPagesAmount = maxPagesAmount;
        }
        pagesNumber = currentRequestPagesAmount;

        log.debug("Searching employers data");
        List<Employer> employers = new ArrayList<>();
        for (int i = 0; i < currentRequestPagesAmount; i++) {
            log.debug("\tstarted loading page " + i);
            String response = HttpUtils.sendHttpRequest(EMPLOYERS_HH_API_URL + "?page=" + i, "getEmployersDataByLocation");
            List<Employer> currentPageEmployers = parseEmployersFromJsonResponse(response);
            employers.addAll(currentPageEmployers);
            log.debug("\tloaded page " + i);
        }
        log.debug(String.format("Employers list built successfully (pages_amount = %d, list_size = %d)", currentRequestPagesAmount, employers.size()));
        return employers;
    }

    public List<Employer> getEmployersByLocation(String location, Optional<Integer> userPagesAmount) {
        log.debug(String.format("getEmployersDataByLocation(%s, %s) invoked", location, userPagesAmount));
        long locationId = locationIdCalculator.findHhLocationIdByName(location);
        log.debug(String.format("calculated locationId(%s) = %d", location, locationId));
        String requestUrl = EMPLOYERS_HH_API_URL + "?area=" + locationId;

        long currentRequestPagesAmount;
        long maxPagesAmount = countJsonResponsePages(requestUrl);
        if (userPagesAmount.isPresent() && userPagesAmount.get() <= maxPagesAmount) {
            currentRequestPagesAmount = userPagesAmount.get();
        } else {
            currentRequestPagesAmount = maxPagesAmount;
        }
        pagesNumber = currentRequestPagesAmount;

        log.debug(String.format("Searching employers in region: %s (id = %d, amount = %d)", location, locationId, currentRequestPagesAmount));
        List<Employer> employers = new ArrayList<>();
        for (int i = 0; i < currentRequestPagesAmount; i++) {
            log.debug("\tstarted loading page " + i);
            String response = HttpUtils.sendHttpRequest(requestUrl + "&page=" + i, "getEmployersDataByLocation");
            List<Employer> currentPageEmployers = parseEmployersFromJsonResponse(response);
            employers.addAll(currentPageEmployers);
            log.debug("\tloaded page " + i);
        }
        log.debug(String.format("Employers list built successfully (location_id = %d, pages_amount = %d, list_size = %d)", locationId, currentRequestPagesAmount, employers.size()));
        return employers;
    }

    public void sortEmployersListByVacancyName(List<Employer> employers, String vacancyName) {
        log.debug("Started sorting employers list by vacancy_name = " + vacancyName + " by deleting wrong employers");
        vacancyName = InputValueFormatter.formatInputValue(vacancyName);
        for (Employer employer : employers) {
            if (employer.getOpenVacanciesAmount() == 0) {
                employerCrud.delete(employer.getId());
            }
        }
        List<Employer> employersWithoutNeededVacancies = employerDao.getAllNotLikeVacancyName(vacancyName.substring(1));
        for (Employer employer : employersWithoutNeededVacancies) {
            employerCrud.delete(employer.getId());
        }
        log.debug("Employers list sorted by vacancy_name: " + vacancyName);
    }

    @Override
    public DataParserDTO initData(Optional<String> vacancyName, Optional<String> location, Optional<Integer> userPagesAmount) {
        DataParserDTO dto = new DataParserDTO();
        final long startTime = System.currentTimeMillis();
        String successDescription = "new data initialisation completed";

        if (vacancyName.isPresent()) {
            dto.setNameFilter(vacancyName.get());
        }
        if (location.isPresent()) {
            dto.setLocationFilter(location.get());
        }

        List<Employer> loadedEmployers;
        try {
            if (location.isPresent()) {
                loadedEmployers = getEmployersByLocation(location.get(), userPagesAmount);
                successDescription += String.format(", sorted by location(%s)", location.get());
            } else {
                loadedEmployers = getAllEmployers(userPagesAmount);
            }
        } catch (Exception e) {
            throw new DataInitialisationException("initData", e.getMessage());
        }
        if (vacancyName.isPresent()) {
            sortEmployersListByVacancyName(loadedEmployers, vacancyName.get());
            successDescription += String.format(", sorted by vacancy_name(%s)", vacancyName.get());
        }

        dto.setStatus("success");
        dto.setLocalDateTime(LocalDateTime.now());
        dto.setTimeSpent(System.currentTimeMillis() - startTime + "ms");
        dto.setDescription("Previous data deleted, " + successDescription);
        dto.setPagesAmount(pagesNumber);
        log.debug(successDescription);
        return dto;
    }
}
