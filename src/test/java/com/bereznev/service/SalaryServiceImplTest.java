package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.SalaryDto;
import com.bereznev.entity.Salary;
import com.bereznev.entity.Vacancy;
import com.bereznev.repository.SalaryRepository;
import com.bereznev.service.impl.SalaryServiceImpl;
import com.bereznev.utils.CurrencyConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalaryServiceImplTest {
    @Mock
    private VacancyService vacancyService;
    @Mock
    private SalaryRepository salaryRepository;

    private SalaryServiceImpl salaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        salaryService = new SalaryServiceImpl(vacancyService, salaryRepository);
    }

    @Test
    void findLowestPaidVacancy_withLocation_ReturnsConvertedVacancy() {
        String vacancyName = "Software Engineer";
        String location = "New York";
        String trimmedLocation = "York";

        Vacancy mockVacancy = new Vacancy();
        mockVacancy.setSalary(new Salary());
        Vacancy convertedVacancy = new Vacancy();
        when(salaryRepository.findFirstVacancyWithMinimalSalaryByLocation(vacancyName, trimmedLocation)).thenReturn(mockVacancy);
        when(CurrencyConverter.convertCurrency(mockVacancy)).thenReturn(convertedVacancy);

        Vacancy result = salaryService.findLowestPaidVacancy(vacancyName, Optional.of(location));

        assertEquals(convertedVacancy, result);
        verify(salaryRepository).findFirstVacancyWithMinimalSalaryByLocation(vacancyName, trimmedLocation);
//        verify(CurrencyConverter).convertCurrency(mockVacancy);
    }

    @Test
    void findLowestPaidVacancy_withoutLocation_ReturnsConvertedVacancy() {
        String vacancyName = "Software Engineer";

        Vacancy mockVacancy = new Vacancy();
        mockVacancy.setSalary(new Salary());
        Vacancy convertedVacancy = new Vacancy();
        when(salaryRepository.findFirstVacancyWithMinimalSalary(vacancyName)).thenReturn(mockVacancy);
        when(CurrencyConverter.convertCurrency(mockVacancy)).thenReturn(convertedVacancy);

        Vacancy result = salaryService.findLowestPaidVacancy(vacancyName, Optional.empty());

        assertEquals(convertedVacancy, result);
        verify(salaryRepository).findFirstVacancyWithMinimalSalary(vacancyName);
//        verify(CurrencyConverter).convertCurrency(mockVacancy);
    }

    @Test
    void findHighestPaidVacancy_withLocation_ReturnsConvertedVacancy() {
        String vacancyName = "Software Engineer";
        String location = "New York";
        String trimmedLocation = "York";

        Vacancy mockVacancy = new Vacancy();
        mockVacancy.setSalary(new Salary());
        Vacancy convertedVacancy = new Vacancy();
        when(salaryRepository.findFirstVacancyWithMaximalSalaryByLocation(vacancyName, trimmedLocation)).thenReturn(mockVacancy);
        when(CurrencyConverter.convertCurrency(mockVacancy)).thenReturn(convertedVacancy);

        Vacancy result = salaryService.findHighestPaidVacancy(vacancyName, Optional.of(location));

        assertEquals(convertedVacancy, result);
        verify(salaryRepository).findFirstVacancyWithMaximalSalaryByLocation(vacancyName, trimmedLocation);
//        verify(CurrencyConverter).convertCurrency(mockVacancy);
    }

    @Test
    void findHighestPaidVacancy_withoutLocation_ReturnsConvertedVacancy() {
        String vacancyName = "Software Engineer";

        Vacancy mockVacancy = new Vacancy();
        mockVacancy.setSalary(new Salary());
        Vacancy convertedVacancy = new Vacancy();
        when(salaryRepository.findFirstVacancyWithMaximalSalary(vacancyName)).thenReturn(mockVacancy);
        when(CurrencyConverter.convertCurrency(mockVacancy)).thenReturn(convertedVacancy);

        Vacancy result = salaryService.findHighestPaidVacancy(vacancyName, Optional.empty());

        assertEquals(convertedVacancy, result);
        verify(salaryRepository).findFirstVacancyWithMaximalSalary(vacancyName);
//        verify(CurrencyConverter).convertCurrency(mockVacancy);
    }

    @Test
    void calculateAverageSalaryValue_withVacancies_ReturnsCorrectAverage() {
        List<Vacancy> vacancies = new ArrayList<>();
        Vacancy vacancy1 = new Vacancy();
        vacancy1.setSalary(new Salary(BigDecimal.valueOf(1000), BigDecimal.valueOf(2000), "RUR"));
        Vacancy vacancy2 = new Vacancy();
        vacancy2.setSalary(new Salary(BigDecimal.valueOf(3000), BigDecimal.valueOf(4000), "RUR"));
        vacancies.add(vacancy1);
        vacancies.add(vacancy2);

        BigDecimal expectedAverage = BigDecimal.valueOf(2500);

        BigDecimal result = salaryService.calculateAverageSalaryValue(vacancies);

        assertEquals(expectedAverage, result);
    }

    @Test
    void calculateAverageSalaryValue_withEmptyVacancies_ReturnsZero() {
        List<Vacancy> vacancies = new ArrayList<>();

        BigDecimal result = salaryService.calculateAverageSalaryValue(vacancies);

        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void getSalaryStatistics_withLocation_ReturnsDtoWithLocation() {
        String vacancyName = "Software Engineer";
        String location = "New York";
        List<Vacancy> vacancies = new ArrayList<>();
        SalaryDto expectedDto = new SalaryDto();

        when(vacancyService.getAllByNameAndLocation(vacancyName, location)).thenReturn(vacancies);
        when(vacancyService.getAllByName(vacancyName)).thenReturn(vacancies);

        SalaryDto result = salaryService.getSalaryStatistics(vacancyName, Optional.of(location));

        assertEquals(expectedDto, result);
        assertEquals(location, result.getLocationFilter());
        verify(vacancyService).getAllByNameAndLocation(vacancyName, location);
        verify(vacancyService, never()).getAllByName(vacancyName);
    }

    @Test
    void getSalaryStatistics_withoutLocation_ReturnsDtoWithoutLocation() {
        String vacancyName = "Software Engineer";
        List<Vacancy> vacancies = new ArrayList<>();
        SalaryDto expectedDto = new SalaryDto();

        when(vacancyService.getAllByName(vacancyName)).thenReturn(vacancies);

        SalaryDto result = salaryService.getSalaryStatistics(vacancyName, Optional.empty());

        assertEquals(expectedDto, result);
        assertNull(result.getLocationFilter());
        verify(vacancyService, never()).getAllByNameAndLocation(anyString(), anyString());
        verify(vacancyService).getAllByName(vacancyName);
    }

    @Test
    void deleteAll_CallsSalaryRepositoryDeleteAll() {
        salaryService.deleteAll();

        verify(salaryRepository).deleteAll();
    }

    @Test
    void save_CallsSalaryRepositorySave() {
        Salary salary = new Salary();
        Salary savedSalary = new Salary();

        when(salaryRepository.save(salary)).thenReturn(savedSalary);

        Salary result = salaryService.save(salary);

        assertEquals(savedSalary, result);
        verify(salaryRepository).save(salary);
    }

    @Test
    void countDatabaseLinesAmount_CallsSalaryRepositoryCount() {
        long expectedCount = 10;

        when(salaryRepository.count()).thenReturn(expectedCount);

        long result = salaryService.countDatabaseLinesAmount();

        assertEquals(expectedCount, result);
        verify(salaryRepository).count();
    }

    @Test
    void saveAll_CallsSalaryRepositorySaveAll() {
        List<Salary> salaries = new ArrayList<>();

        salaryService.saveAll(salaries);

        verify(salaryRepository).saveAll(salaries);
    }
}
