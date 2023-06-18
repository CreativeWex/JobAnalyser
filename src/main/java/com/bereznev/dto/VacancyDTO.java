package com.bereznev.dto;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Vacancy;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class VacancyDTO {
    @JsonProperty("time_spent")
    private String timeSpent;

    @JsonProperty("filtered_by_name")
    private String nameFilter = null;

    @JsonProperty("filtered_by_location")
    private String locationFilter = null;

    @JsonProperty("vacancies_number")
    private int vacanciesNumber;

    @JsonProperty("vacancies")
    private List<Vacancy> vacancies;
}
