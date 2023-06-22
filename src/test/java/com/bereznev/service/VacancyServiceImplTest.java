package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Vacancy;
import com.bereznev.exception.ResourceNotFoundException;
import com.bereznev.repository.VacancyRepository;
import com.bereznev.service.impl.VacancyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class VacancyServiceImplTest {
    @Mock
    private VacancyRepository vacancyRepository;

    @InjectMocks
    private VacancyServiceImpl vacancyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_ReturnsAllVacancies() {
        List<Vacancy> expectedVacancies = new ArrayList<>();
        expectedVacancies.add(new Vacancy());
        expectedVacancies.add(new Vacancy());

        when(vacancyRepository.findAll()).thenReturn(expectedVacancies);

        List<Vacancy> result = vacancyService.getAll();

        assertEquals(expectedVacancies, result);
        verify(vacancyRepository).findAll();
    }

    @Test
    void getAllByName_ReturnsVacanciesWithMatchingName() {
        String vacancyName = "Software Engineer";
        List<Vacancy> expectedVacancies = new ArrayList<>();
        expectedVacancies.add(new Vacancy());
        expectedVacancies.add(new Vacancy());

        when(vacancyRepository.getAllByName(vacancyName)).thenReturn(expectedVacancies);

        List<Vacancy> result = vacancyService.getAllByName(vacancyName);

        assertEquals(expectedVacancies, result);
        verify(vacancyRepository).getAllByName(vacancyName.substring(1));
    }

    @Test
    void getAllByNameAndLocation_ReturnsVacanciesWithMatchingNameAndLocation() {
        String vacancyName = "Software Engineer";
        String location = "New York";
        List<Vacancy> expectedVacancies = new ArrayList<>();
        expectedVacancies.add(new Vacancy());
        expectedVacancies.add(new Vacancy());

        when(vacancyRepository.getAllByNameAndLocation(vacancyName, location)).thenReturn(expectedVacancies);

        List<Vacancy> result = vacancyService.getAllByNameAndLocation(vacancyName, location);

        assertEquals(expectedVacancies, result);
        verify(vacancyRepository).getAllByNameAndLocation(vacancyName.substring(1), location.substring(1));
    }

    @Test
    void getById_WithExistingId_ReturnsVacancy() {
        long vacancyId = 1;
        Vacancy expectedVacancy = new Vacancy();

        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.of(expectedVacancy));

        Vacancy result = vacancyService.getById(vacancyId);

        assertEquals(expectedVacancy, result);
        verify(vacancyRepository).findById(vacancyId);
    }

    @Test
    void getById_WithNonExistingId_ThrowsResourceNotFoundException() {
        long vacancyId = 1;

        when(vacancyRepository.findById(vacancyId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vacancyService.getById(vacancyId));
        verify(vacancyRepository).findById(vacancyId);
    }

    @Test
    void saveAll_CallsVacancyRepositorySaveAll() {
        List<Vacancy> vacancies = new ArrayList<>();

        vacancyService.saveAll(vacancies);

        verify(vacancyRepository).saveAll(vacancies);
    }

    @Test
    void deleteAll_CallsVacancyRepositoryDeleteAll() {
        vacancyService.deleteAll();

        verify(vacancyRepository).deleteAll();
    }

    @Test
    void save_CallsVacancyRepositorySave() {
        Vacancy vacancy = new Vacancy();

        vacancyService.save(vacancy);

        verify(vacancyRepository).save(vacancy);
    }

    @Test
    void countDatabaseLinesAmount_CallsVacancyRepositoryCount() {
        long expectedCount = 10;

        when(vacancyRepository.count()).thenReturn(expectedCount);

        long result = vacancyService.countDatabaseLinesAmount();

        assertEquals(expectedCount, result);
        verify(vacancyRepository).count();
    }
}

