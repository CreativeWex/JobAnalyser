package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.crud.SalaryCrud;
import com.bereznev.crud.SkillCrud;
import com.bereznev.crud.VacancyCrud;
import com.bereznev.entity.Employer;
import com.bereznev.entity.Salary;
import com.bereznev.entity.Skill;
import com.bereznev.entity.Vacancy;
import com.bereznev.service.VacanciesInitializer;
import com.bereznev.mapper.VacanciesMapper;
import com.bereznev.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Log4j
@Service
public class VacanciesInitializerImpl implements VacanciesInitializer {
    private static final String VACANCY_API_URL = "https://api.hh.ru/vacancies";

    private final VacancyCrud vacancyCrud;
    private final SalaryCrud salaryCrud;
    private final SkillCrud skillCrud;

    @Autowired
    public VacanciesInitializerImpl(VacancyCrud vacancyCrud, SalaryCrud salaryCrud, SkillCrud skillCrud) {
        this.vacancyCrud = vacancyCrud;
        this.salaryCrud = salaryCrud;
        this.skillCrud = skillCrud;
    }

    @Override
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

    @Override
    public Vacancy fillVacancyFields(Vacancy vacancy) {
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
        return vacancy;
    }

    @Override
    public void fillKeySkillsForVacancy(JsonObject jsonObject, Vacancy vacancy) {
        JsonArray keySkillsArray = jsonObject.getAsJsonArray("key_skills");
        for (int i = 0; i < keySkillsArray.size(); i++) {
            JsonObject skillObject = keySkillsArray.get(i).getAsJsonObject();
            Skill skill = new Skill(skillObject.get("name").getAsString());
            skill.setVacancy(vacancy);
            vacancy.getSkills().add(skill);
        }
    }

    @Override
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
}
