package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.SalaryDTO;

public interface SalaryService {

    public SalaryDTO getSalaryStatistics(String vacancyName); //TODO: баг при 2 и более словах

    public SalaryDTO getSalaryStatisticsByLocation(String vacancyName, String location); //TODO: баг при 2 и более словах

}
