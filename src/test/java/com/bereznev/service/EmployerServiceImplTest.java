package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Employer;
import com.bereznev.exception.ResourceNotFoundException;
import com.bereznev.repository.EmployerRepository;
import com.bereznev.service.impl.EmployerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class EmployerServiceImplTest {

    @Mock
    private EmployerRepository employerRepository;

    @InjectMocks
    private EmployerServiceImpl employerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteAll() {
        employerService.deleteAll();
        verify(employerRepository, times(1)).deleteAll();
    }

    @Test
    void testCountDatabaseLinesAmount() {
        long expectedCount = 10;
        when(employerRepository.count()).thenReturn(expectedCount);

        long count = employerService.countDatabaseLinesAmount();

        Assertions.assertEquals(expectedCount, count);
    }

    @Test
    void testGetById() {
        long employerId = 1L;
        Employer expectedEmployer = new Employer();
        when(employerRepository.findById(employerId)).thenReturn(Optional.of(expectedEmployer));

        Employer employer = employerService.getById(employerId);

        Assertions.assertEquals(expectedEmployer, employer);
    }

    @Test
    void testGetById_ThrowsResourceNotFoundException() {
        long employerId = 1L;
        when(employerRepository.findById(employerId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employerService.getById(employerId);
        });
    }

    @Test
    void testSave() {
        Employer employer = new Employer();
        when(employerRepository.save(employer)).thenReturn(employer);

        Employer savedEmployer = employerService.save(employer);

        Assertions.assertEquals(employer, savedEmployer);
        verify(employerRepository, times(1)).save(employer);
    }

    @Test
    void testSaveAll() {
        List<Employer> employers = new ArrayList<>();
        Employer employer1 = new Employer();
        Employer employer2 = new Employer();
        employers.add(employer1);
        employers.add(employer2);

        employerService.saveAll(employers);

        verify(employerRepository, times(1)).saveAll(employers);
    }

    @Test
    void testGetAll() {
        List<Employer> expectedEmployers = new ArrayList<>();
        when(employerRepository.findAll()).thenReturn(expectedEmployers);

        List<Employer> employers = employerService.getAll();

        Assertions.assertEquals(expectedEmployers, employers);
    }

    @Test
    void testGetAllFilteredByVacancy() {
        String vacancyName = "Software Engineer";
        List<Employer> expectedEmployers = new ArrayList<>();
        when(employerRepository.getAllByVacancyName(vacancyName)).thenReturn(expectedEmployers);

        List<Employer> employers = employerService.getAllFilteredByVacancy(vacancyName);

        Assertions.assertEquals(expectedEmployers, employers);
    }

    @Test
    void testGetAllFilteredByVacancyAndLocation() {
        String vacancyName = "Software Engineer";
        String location = "New York";
        List<Employer> expectedEmployers = new ArrayList<>();
        when(employerRepository.getAllByVacancyNameAndLocation(vacancyName, location)).thenReturn(expectedEmployers);

        List<Employer> employers = employerService.getAllFilteredByVacancyAndLocation(vacancyName, location);

        Assertions.assertEquals(expectedEmployers, employers);
    }
}
