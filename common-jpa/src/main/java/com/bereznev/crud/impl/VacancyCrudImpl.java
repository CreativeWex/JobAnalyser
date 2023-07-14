package com.bereznev.crud.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Vacancy;
import com.bereznev.exceptions.logic.ResourceNotFoundException;
import com.bereznev.repository.VacancyRepository;
import com.bereznev.crud.VacancyCrud;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
public class VacancyCrudImpl implements VacancyCrud {
    private final VacancyRepository vacancyRepository;
    private static final String RESOURCE_NAME = "Vacancy";

    @Autowired
    public VacancyCrudImpl(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @Override
    @Cacheable(value = "vacancies")
    public List<Vacancy> getAll() {
        return vacancyRepository.findAll();
    }

    @Override
    @Cacheable(value = "vacanciesByName")
    public List<Vacancy> getAllByName(String vacancyName) {
        return vacancyRepository.getAllByName(vacancyName.substring(1));
    }

    @Override
    @Cacheable(value = "vacanciesByLocation")
    public List<Vacancy> getAllByLocation(String location) {
        return vacancyRepository.getAllByLocation(location.substring(1));
    }

    @Override
    @Cacheable(value = "vacanciesByNameAndLocation")
    public List<Vacancy> getAllByNameAndLocation(String vacancyName, String location) {
        return vacancyRepository.getAllByNameAndLocation(vacancyName.substring(1), location.substring(1));
    }

    @Override
    @Cacheable(value = "vacancies", key = "#vacancyId")
    public Vacancy getById(long vacancyId) {
        return vacancyRepository.findById(vacancyId).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Id", vacancyId));
    }

    @Override
    @CachePut(value = "vacancies", key = "#result.id")
    public void saveAll(List<Vacancy> vacancies) {
        vacancyRepository.saveAll(vacancies);
    }

    @Override
    @CacheEvict(value = "vacancies")
    public void deleteAll() {
        try {
            vacancyRepository.deleteAll();
            log.debug("All vacancies data deleted");
        } catch (Exception e) {
            log.error("Error deleting vacancies data: " + e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "vacancies", key = "#id")
    public void delete(long id) {
        vacancyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Id", id));
        vacancyRepository.deleteById(id);
        log.debug("deleted, id: " + id);
    }

    @Override
    @CachePut(value = "vacancies", key = "#result.id")
    public Vacancy save(Vacancy vacancy) {
        return vacancyRepository.save(vacancy);
    }

    @Override
    public long countDatabaseLinesAmount() {
        return vacancyRepository.count();
    }
}
