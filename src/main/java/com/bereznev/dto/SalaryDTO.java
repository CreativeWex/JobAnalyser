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

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SalaryDTO {
    private String currency;

    @JsonProperty("vacancies_found")
    private int vacanciesFound;

    @JsonProperty("minimal_salary_limit")
    private BigDecimal minimalSalaryLimit;

    @JsonProperty("maximum_salary_limit")
    private BigDecimal maximumSalaryLimit;

    @JsonProperty("average_value")
    private BigDecimal averageValue;

    @JsonProperty("lowest_paid_vacancy")
    private Vacancy lowestPaidVacancy;

    @JsonProperty("highest_salary_limit")
    private Vacancy highestPaidVacancy;
}
