package com.bereznev.dto;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.model.Vacancy;
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

    @JsonProperty("vacancies found")
    private int vacanciesFound;

    @JsonProperty("minimal salary limit")
    private BigDecimal minimalSalaryLimit;

    @JsonProperty("maximum salary limit")
    private BigDecimal maximumSalaryLimit;

    @JsonProperty("average value")
    private BigDecimal averageValue;

    @JsonProperty("lowest paid vacancy")
    private Vacancy lowestPaidVacancy;

    @JsonProperty("highest salary limit")
    private Vacancy highestPaidVacancy;
}
