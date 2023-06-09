package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.vacancies.SalaryDTO;

public interface SalaryService {

    public SalaryDTO getSalaryStatistics(String vacancyName);

    public SalaryDTO getSalaryStatisticsByLocation(String vacancyName, String location);

}
