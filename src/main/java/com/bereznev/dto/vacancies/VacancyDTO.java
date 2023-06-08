package com.bereznev.dto.vacancies;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.model.Employer;
import com.bereznev.model.Vacancy;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacancyDTO {

    @JsonProperty("found")
    private int vacanciesNumber;

    @JsonProperty("items")
    private Set<Vacancy> vacancies;
}
