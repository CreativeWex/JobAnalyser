package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.SalaryDTO;
import com.bereznev.entity.Salary;
import com.bereznev.entity.Vacancy;
import com.bereznev.repository.SalaryRepository;
import com.bereznev.service.SalaryService;
import com.bereznev.service.VacancyService;
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
    private final VacancyService vacancyService;
    private final SalaryRepository salaryRepository;

    @Autowired
    public SalaryServiceImpl(VacancyService vacancyService, SalaryRepository salaryRepository) {
        this.vacancyService = vacancyService;
        this.salaryRepository = salaryRepository;
    }

    @Override
    public SalaryDTO getSalaryStatistics(String vacancyName, Optional<String> location) {
        SalaryDTO dto = new SalaryDTO();
        dto.setNameFilter(vacancyName);
        dto.setCurrency("RUR");
        dto.setHighestPaidVacancy(findHighestPaidVacancy(location));
        dto.setLowestPaidVacancy(findLowestPaidVacancy(location));
        dto.setMaximumSalaryLimit(dto.getHighestPaidVacancy().getSalary().getMaximumAmount());
        dto.setMinimalSalaryLimit(dto.getLowestPaidVacancy().getSalary().getMinimalAmount());
        dto.setVacanciesFound(vacancyService.getAllByName(vacancyName).size());
        if (location.isPresent()) {
            dto.setAverageValue(calculateAverageSalaryValue(vacancyService.getAllByNameAndLocation(vacancyName, location.get())));
            dto.setLocationFilter(location.get());
            return dto;
        }
        dto.setAverageValue(calculateAverageSalaryValue(vacancyService.getAllByName(vacancyName)));
        return dto;
    }

    @Override
    public void deleteAll() {
        try {
            salaryRepository.deleteAll();
            log.debug("All salaries data deleted");
        } catch (Exception e) {
            log.error("Error deleting salaries data: " + e.getMessage());
        }
    }

    @Override
    public Salary save(Salary salary) {
        return salaryRepository.save(salary);
    }

    @Override
    public long countDatabaseLinesAmount() {
        return salaryRepository.count();
    }

    @Override
    public void saveAll(List<Salary> salaries) {
        salaryRepository.saveAll(salaries);
    }

    public Vacancy findLowestPaidVacancy(Optional<String> location) {
        if (location.isPresent()) {
            Vacancy lowestPaidVacancy = salaryRepository.findFirstVacancyWithMinimalSalaryByLocation(location.get().substring(1));
            return CurrencyConverter.convertCurrency(lowestPaidVacancy);
        }
        Vacancy lowestPaidVacancy = salaryRepository.findFirstVacancyWithMinimalSalary();
        return CurrencyConverter.convertCurrency(lowestPaidVacancy);
    }

    public Vacancy findHighestPaidVacancy(Optional<String> location) {
        if (location.isPresent()) {
            Vacancy highestPaidVacancy = salaryRepository.findFirstVacancyWithMaximalSalaryByLocation(location.get().substring(1));
            return CurrencyConverter.convertCurrency(highestPaidVacancy);
        }
        Vacancy highestPaidVacancy = salaryRepository.findFirstVacancyWithMaximalSalary();
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
}
