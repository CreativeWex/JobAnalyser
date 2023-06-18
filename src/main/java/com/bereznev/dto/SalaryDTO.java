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
    @JsonProperty("time_spent")
    private String timeSpent;

    @JsonProperty("filtered_by_name")
    private String nameFilter = null;

    @JsonProperty("filtered_by_location")
    private String locationFilter = null;

    @JsonProperty("salary_currency")
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

    public SalaryDTO(String currency, int vacanciesFound, BigDecimal minimalSalaryLimit, BigDecimal maximumSalaryLimit,
                     BigDecimal averageValue, Vacancy lowestPaidVacancy, Vacancy highestPaidVacancy) {
        this.currency = currency;
        this.vacanciesFound = vacanciesFound;
        this.minimalSalaryLimit = minimalSalaryLimit;
        this.maximumSalaryLimit = maximumSalaryLimit;
        this.averageValue = averageValue;
        this.lowestPaidVacancy = lowestPaidVacancy;
        this.highestPaidVacancy = highestPaidVacancy;
    }
}
