package com.bereznev.dto.vacancies;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.model.Vacancy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SalaryDTO {

    private String currency;

    private BigDecimal lowestLimit;

    private Vacancy lowestSalaryVacancy;

    private BigDecimal highestLimit;

    private Vacancy highestSalaryVacancy;

    private BigDecimal avgValue;
}
