package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.crud.VacancyCrud;
import com.bereznev.dto.SalaryDto;
import com.bereznev.entity.Vacancy;
import com.bereznev.exceptions.logic.ResourceNotFoundException;
import com.bereznev.repository.SalaryRepository;
import com.bereznev.service.SalaryService;
import com.bereznev.utils.CurrencyConverter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Log4j
@Service
public class SalaryServiceImpl implements SalaryService {
    private final VacancyCrud vacancyCrud;
    private final SalaryRepository salaryRepository;

    @Autowired
    public SalaryServiceImpl(VacancyCrud vacancyCrud, SalaryRepository salaryRepository) {
        this.vacancyCrud = vacancyCrud;
        this.salaryRepository = salaryRepository;
    }

    public Vacancy findLowestPaidVacancy(String vacancyName, Optional<String> location) {
        if (location.isPresent()) {
            Vacancy lowestPaidVacancy = salaryRepository.findFirstVacancyWithMinimalSalaryByLocation(vacancyName, location.get().substring(1));
            return CurrencyConverter.convertCurrency(lowestPaidVacancy);
        }
        Vacancy lowestPaidVacancy = salaryRepository.findFirstVacancyWithMinimalSalary(vacancyName);
        return CurrencyConverter.convertCurrency(lowestPaidVacancy);
    }

    public Vacancy findHighestPaidVacancy(String vacancyName, Optional<String> location) {
        if (location.isPresent()) {
            Vacancy highestPaidVacancy = salaryRepository.findFirstVacancyWithMaximalSalaryByLocation(vacancyName, location.get().substring(1));
            return CurrencyConverter.convertCurrency(highestPaidVacancy);
        }
        Vacancy highestPaidVacancy = salaryRepository.findFirstVacancyWithMaximalSalary(vacancyName);
        return CurrencyConverter.convertCurrency(highestPaidVacancy);
    }

    public BigDecimal calculateAverageSalaryValue(List<Vacancy> vacancies) {
        BigDecimal averageForkSum = BigDecimal.ZERO;
        if (vacancies.isEmpty()) {
            return BigDecimal.ZERO;
        }
        for (Vacancy vacancy : vacancies) {
            BigDecimal minimalPrice = vacancy.getSalary().getMinimalAmount();
            BigDecimal maximalPrice = vacancy.getSalary().getMaximumAmount();
            averageForkSum = averageForkSum.add(minimalPrice.add(maximalPrice).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP));
        }
        return averageForkSum.divide(BigDecimal.valueOf(vacancies.size()), RoundingMode.HALF_UP);
    }
    @Override
    public SalaryDto getSalaryStatistics(String vacancyName, Optional<String> location) {
        SalaryDto dto = new SalaryDto();
        long startTime = System.currentTimeMillis();

        dto.setNameFilter(vacancyName);
        dto.setCurrency("RUR");
        try {
            dto.setHighestPaidVacancy(findHighestPaidVacancy(vacancyName.substring(1), location));
            dto.setLowestPaidVacancy(findLowestPaidVacancy(vacancyName.substring(1), location));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Vacancies", "vacancyName / location", vacancyName + "/" + location);
        }
        dto.setMaximumSalaryLimit(dto.getHighestPaidVacancy().getSalary().getMaximumAmount());
        dto.setMinimalSalaryLimit(dto.getLowestPaidVacancy().getSalary().getMinimalAmount());
        dto.setVacanciesFound(vacancyCrud.getAllByName(vacancyName).size());
        if (location.isPresent()) {
            dto.setAverageValue(calculateAverageSalaryValue(vacancyCrud.getAllByNameAndLocation(vacancyName, location.get())));
            dto.setLocationFilter(location.get());
            return dto;
        }
        dto.setAverageValue(calculateAverageSalaryValue(vacancyCrud.getAllByName(vacancyName)));
        dto.setTimeSpent(System.currentTimeMillis() - startTime + " ms");

        return dto;
    }
}
