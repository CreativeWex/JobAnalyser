package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.crud.EmployerCrud;
import com.bereznev.crud.SalaryCrud;
import com.bereznev.crud.SkillCrud;
import com.bereznev.crud.VacancyCrud;
import com.bereznev.entity.Skill;
import com.bereznev.exceptions.logic.DataInitialisationException;
import com.bereznev.mapper.EmployersMapper;
import com.bereznev.mapper.VacanciesMapper;
import com.bereznev.entity.Employer;
import com.bereznev.entity.Salary;
import com.bereznev.entity.Vacancy;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j
@Service
@Transactional
public class DataInitializerImpl implements DataInitializer {
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

    public void initEmployersSortByVacancyName(String vacancyName) {
        int employersPageAmount = countJSONResponsePages(EMPLOYERS_API_URL + "?name=" + vacancyName);
        List<Employer> employers = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 2; i++) { //FIXME employersPageAmount
            log.debug("Loaded page " + i);
            String response = HttpUtils.sendHttpRequest(EMPLOYERS_API_URL + "?name=" + vacancyName + "&page=" + i,
                    "EmployerServiceImpl (getAll())");
            List<Employer> currentPageEmployers = parseEmployersFromJSON(response);
            employers.addAll(currentPageEmployers);
        }
        employerCrud.saveAll(employers);
        log.debug(String.format("Employers list built (size = %d, vacancy name = %s), time: %d ms", employers.size(), vacancyName, System.currentTimeMillis() - startTime));
    }

    public void initAllEmployers() {
        int employersPageAmount = countJSONResponsePages(EMPLOYERS_API_URL);
        List<Employer> employers = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 2; i++) { //FIXME employersPageAmount
            log.debug("Loaded page " + i);
            String response = HttpUtils.sendHttpRequest(EMPLOYERS_API_URL + "?page=" + i, "EmployerServiceImpl (getAll())");
            List<Employer> currentPageEmployers = parseEmployersFromJSON(response);
            employers.addAll(currentPageEmployers);
        }
        employerCrud.saveAll(employers);
        log.debug(String.format("Employers list built (size = %d), time: %d ms", employers.size(), System.currentTimeMillis() - startTime));
    }

    @Override
    public void initData(Optional<String> vacancyName) {
        log.debug("initData invoked");
        deleteAllData(); //FIXME
        long startTime = System.currentTimeMillis();
        try {
            if (vacancyName.isPresent()) {
                initEmployersSortByVacancyName(vacancyName.get());
            } else {
                initAllEmployers();
            }
        } catch (Exception e) {
            throw new DataInitialisationException("initData", e.getMessage());
        }
        log.debug(String.format("Data initialisation completed, time: %d ms", System.currentTimeMillis() - startTime));
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
}
