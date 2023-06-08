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

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SalaryDTO {

    private double lowestLimit;

    private Vacancy lowestSalaryVacancy;

    private double highestLimit;

    private Vacancy highestSalaryVacancy;

    private double avgValue;
}
