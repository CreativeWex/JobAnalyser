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
    public SalaryDTO getSalaryStatistics(String vacancyName) {
        return calculateMinMaxAvgValues(vacancyService.getAllByName(vacancyName));
    }

    @Override
    public SalaryDTO getSalaryStatisticsByLocation(String vacancyName, String location) {
        return calculateMinMaxAvgValues(vacancyService.getAllByNameAndLocation(vacancyName, location));
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

    public SalaryDTO calculateMinMaxAvgValues(List<Vacancy> vacancies) {
        BigDecimal minimalSalaryLimit = BigDecimal.TEN.pow(10);
        BigDecimal maximumSalaryLimit = BigDecimal.ZERO;
        BigDecimal middleForkValue = BigDecimal.ZERO;
        Vacancy lowestPaidVacancy = null;
        Vacancy highestPaidVacancy = null;
        int iterationNumber = 0;

        for (Vacancy vacancy : vacancies) {
             if (!vacancy.getSalary().getCurrency().equals("RUR")) {
                if (vacancy.getSalary().getCurrency().equals("BYR")) {
                    vacancy.getSalary().setCurrency("BYN");
                }
                CurrencyConverter.convertCurrency(vacancy);
            }
            BigDecimal startPrice = vacancy.getSalary().getMinimalAmount();
            BigDecimal finishPrice = vacancy.getSalary().getMaximumAmount();
            BigDecimal middlePrice = startPrice.add(finishPrice).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);

            if (minimalSalaryLimit.compareTo(startPrice) > 0) {
                minimalSalaryLimit = startPrice;
                lowestPaidVacancy = vacancy;
            }
            if (maximumSalaryLimit.compareTo(finishPrice) < 0) {
                maximumSalaryLimit = finishPrice;
                highestPaidVacancy = vacancy;
            }
            middleForkValue = middleForkValue.add(middlePrice);
            iterationNumber++;
        }
        BigDecimal averageValue;
        if (iterationNumber == 0) {
            averageValue = BigDecimal.ZERO;
        } else {
            averageValue = middleForkValue.divide(BigDecimal.valueOf(iterationNumber), RoundingMode.HALF_UP);
        }
        return new SalaryDTO("RUR", iterationNumber, minimalSalaryLimit, maximumSalaryLimit, averageValue,
                lowestPaidVacancy, highestPaidVacancy);
    }
}
