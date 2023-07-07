package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.crud.EmployerCrud;
import com.bereznev.crud.SalaryCrud;
import com.bereznev.crud.SkillCrud;
import com.bereznev.crud.VacancyCrud;
import com.bereznev.dto.DataInitializerDTO;
import com.bereznev.entity.Skill;
import com.bereznev.exceptions.logic.DataInitialisationException;
import com.bereznev.mapper.EmployersMapper;
import com.bereznev.mapper.VacanciesMapper;
import com.bereznev.entity.Employer;
import com.bereznev.entity.Salary;
import com.bereznev.entity.Vacancy;
import com.bereznev.service.DataInitializer;
import com.bereznev.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j
@Service
@Transactional
public class DataInitializerImpl implements DataInitializer {
    private Integer pagesAmount = 0;
    private String currentEmployerRequestUrl;

    private static final String VACANCY_API_URL = "https://api.hh.ru/vacancies";
    private static final String EMPLOYERS_API_URL = "https://api.hh.ru/employers";

    private final EmployerCrud employerCrud;
    private final VacancyCrud vacancyCrud;
    private final SalaryCrud salaryCrud;
    private final SkillCrud skillCrud;

    @Autowired
    public DataInitializerImpl(EmployerCrud employerCrud, VacancyCrud vacancyCrud, SalaryCrud salaryCrud, SkillCrud skillCrud) {
        this.employerCrud = employerCrud;
        this.vacancyCrud = vacancyCrud;
        this.salaryCrud = salaryCrud;
        this.skillCrud = skillCrud;
    }

    public int countJSONResponsePages(String url) {
        String response = HttpUtils.sendHttpRequest( url, "DataInitializerImpl (countJSONResponsePages)");
        return new JSONObject(response).getInt("pages");
    }

    public void fillSalaryForVacancy(JSONObject jsonObject, Vacancy vacancy) {
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
        salary.setVacancy(vacancy);
    }

    public void fillKeySkillsForVacancy(JsonObject jsonObject, Vacancy vacancy) {
        JsonArray keySkillsArray = jsonObject.getAsJsonArray("key_skills");
        for (int i = 0; i < keySkillsArray.size(); i++) {
            JsonObject skillObject = keySkillsArray.get(i).getAsJsonObject();
            Skill skill = new Skill(skillObject.get("name").getAsString());
            skill.setVacancy(vacancy);
            vacancy.getSkills().add(skill);
        }
    }

    public void fillVacancyFields(Vacancy vacancy) {
        String response = HttpUtils.sendHttpRequest(VACANCY_API_URL + "/" + vacancy.getId(),
                "DataInitializerImpl (fillVacanciesForEmployer)");
        JSONObject jsonObject = new JSONObject(response);
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
        JSONObject addressJson = jsonObject.optJSONObject("address");
        if (addressJson != null) {
            vacancy.setFullAddress(addressJson.optString("raw"));
        }
        String description =  jsonObject.optString("description");
        if (description != null) {
            vacancy.setDescription(description.replaceAll("\\<.*?\\>", ""));
        }
        fillSalaryForVacancy(jsonObject, vacancy);
        fillKeySkillsForVacancy(JsonParser.parseString(response).getAsJsonObject(), vacancy);

        vacancyCrud.save(vacancy);
        salaryCrud.save(vacancy.getSalary());
    }

    public void fillVacanciesForEmployer(Employer employer) {
        if (employer.getOpenVacanciesAmount() == 0) {
            return;
        }
        String response = HttpUtils.sendHttpRequest(VACANCY_API_URL + "?employer_id=" + employer.getId(),
                "DataInitializerImpl (fillVacanciesForEmployer)");
        Gson gson = new Gson();
        List<Vacancy> vacancies = gson.fromJson(response, VacanciesMapper.class).getItems();
        for (Vacancy vacancy : vacancies) {
            fillVacancyFields(vacancy);
        }
    }

    public List<Employer> parseEmployersFromJSON(String jsonResponse) {
        Gson gson = new Gson();
        EmployersMapper employersMapper = gson.fromJson(jsonResponse, EmployersMapper.class);
        List<Employer> employers = employersMapper.getItems();
        for (Employer employer : employers) {
            String getByIdResponse = HttpUtils.sendHttpRequest(EMPLOYERS_API_URL + "/" + employer.getId(),
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
            fillVacanciesForEmployer(employer);
        }
        return employers;
    }

    public void initEmployersByVacancyName(String vacancyName) {
        List<Employer> employers = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < pagesAmount; i++) {
            log.debug("\tstarted loading page " + i);
            String response = HttpUtils.sendHttpRequest(EMPLOYERS_API_URL + "?name=" + vacancyName + "&page=" + i,
                    "EmployerServiceImpl (getAll())");
            List<Employer> currentPageEmployers = parseEmployersFromJSON(response);
            employers.addAll(currentPageEmployers);
            log.debug("\tloaded page " + i);
        }
        employerCrud.saveAll(employers);
        log.debug(String.format("Employers list built (size = %d, vacancy name = %s), time: %d ms", employers.size(), vacancyName, System.currentTimeMillis() - startTime));
    }

    public void initEmployersByLocation(String location) {
        List<Employer> employers = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < pagesAmount; i++) {
            log.debug("started loading page " + i);
            String response = HttpUtils.sendHttpRequest(EMPLOYERS_API_URL + "?page=" + i + "&area=" + location, "initEmployersByLocation");
            List<Employer> currentPageEmployers = parseEmployersFromJSON(response);
            employers.addAll(currentPageEmployers);
            log.debug("loaded page " + i);
        }
        employerCrud.saveAll(employers);
        log.debug(String.format("Employers list built (size = %d), time: %d ms", employers.size(), System.currentTimeMillis() - startTime));
    }

    public void initEmployers() {
        List<Employer> employers = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < pagesAmount; i++) {
            log.debug("started loading page " + i);
            String response = HttpUtils.sendHttpRequest(EMPLOYERS_API_URL + "?page=" + i, "initEmployers");
            List<Employer> currentPageEmployers = parseEmployersFromJSON(response);
            employers.addAll(currentPageEmployers);
            log.debug("loaded page " + i);
        }
        employerCrud.saveAll(employers);
        log.debug(String.format("Employers list built (size = %d), time: %d ms", employers.size(), System.currentTimeMillis() - startTime));
    }

    @Override
    @Transactional
    public void deleteAllData() {
        log.debug("deleteAllData invoked");
        long startTime = System.currentTimeMillis();

        try {
            if (vacancyCrud.countDatabaseLinesAmount() > 0) {
                vacancyCrud.deleteAll();
            }
            if (salaryCrud.countDatabaseLinesAmount() > 0) {
                salaryCrud.deleteAll();
            }
            if (employerCrud.countDatabaseLinesAmount() > 0) {
                employerCrud.deleteAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataInitialisationException("deleteAllData", e.getMessage());
        }
        log.debug(String.format("All data removed successfully, time: %d ms", System.currentTimeMillis() - startTime));
    }


    private Integer calculatePagesAmount(Optional<String> vacancyName, Optional<String> location, Optional<Integer> userPagesAmount) {
        int maxPagesAmount;

        if (vacancyName.isPresent() && location.isPresent()) {
            currentEmployerRequestUrl = EMPLOYERS_API_URL; //Fixme
        } else if (vacancyName.isPresent()) {
//            currentEmployerRequestUrl = EMPLOYERS_API_URL + "?name=" + vacancyName.get();
        } else if (location.isPresent()) {
            currentEmployerRequestUrl = EMPLOYERS_API_URL + "?area=" + location.get();
        } else {
            currentEmployerRequestUrl = EMPLOYERS_API_URL;
        }
        maxPagesAmount = countJSONResponsePages(currentEmployerRequestUrl);
        if (userPagesAmount.isPresent() && userPagesAmount.get() <= maxPagesAmount) {
            return userPagesAmount.get();
        }
        return maxPagesAmount;
    }

    @Override
    public DataInitializerDTO refreshData(Optional<String> vacancyName, Optional<String> location, Optional<Integer> userPagesAmount) {
        DataInitializerDTO dto = new DataInitializerDTO();
        final long startTime = System.currentTimeMillis();

        deleteAllData(); //FIXME
        vacancyName.ifPresent(dto::setNameFilter);
        location.ifPresent(dto::setLocationFilter);
        pagesAmount = calculatePagesAmount(vacancyName, location, userPagesAmount);
        log.debug("pages amount = " + pagesAmount);
        String successDescription = "new data initialisation completed";

        try {
            if (vacancyName.isPresent() && location.isPresent()) {
                successDescription = String.format(", filtered by vacancy name = %s and location = %s!", vacancyName.get(), location.get());
                log.debug("initEmployersByVacancyNameAndLocation(vacancyName.get(), location.get());");
//                initEmployersByVacancyNameAndLocation(vacancyName.get(), location.get());
            } else if (vacancyName.isPresent()) {
                successDescription = String.format(", filtered by vacancy name = %s", vacancyName.get());
                initEmployersByVacancyName(vacancyName.get());
            } else if (location.isPresent()) {
                successDescription = String.format(", filtered by location = %s", location.get());
                log.debug("initEmployersByLocation(location.get());");
//                initEmployersByLocation(location.get());
            } else {
                initEmployers();
            }
        } catch (Exception e) {
            throw new DataInitialisationException("initData", e.getMessage());
        }
        dto.setStatus("success");
        dto.setLocalDateTime(LocalDateTime.now());
        dto.setTimeSpent(System.currentTimeMillis() - startTime + "ms");
        dto.setDescription("Previous data deleted, " + successDescription);
        dto.setPagesAmount(pagesAmount);
        return dto;
    }
}
