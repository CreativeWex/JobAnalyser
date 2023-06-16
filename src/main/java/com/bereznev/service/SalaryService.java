package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.SalaryDTO;
import com.bereznev.entity.Salary;

import java.util.List;

public interface SalaryService {

    public SalaryDTO getSalaryStatistics(String vacancyName); //TODO: баг при 2 и более словах

    public SalaryDTO getSalaryStatisticsByLocation(String vacancyName, String location); //TODO: баг при 2 и более словах

    public void deleteAll();

    public Salary save(Salary salary);

    public long countDatabaseLinesAmount();

    public void saveAll(List<Salary> salaries);

}
