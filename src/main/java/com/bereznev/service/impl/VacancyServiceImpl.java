package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.SkillsDto;
import com.bereznev.entity.Vacancy;
import com.bereznev.exception.ResourceNotFoundException;
import com.bereznev.repository.VacancyRepository;
import com.bereznev.service.VacancyService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j
@Service
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepository vacancyRepository;

    @Autowired
    public VacancyServiceImpl(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @Override
    public List<Vacancy> getAll() {
        return vacancyRepository.findAll();
    }

    @Override
    public List<Vacancy> getAllByName(String vacancyName) {
        return vacancyRepository.getAllByName(vacancyName.substring(1));
    }

    @Override
    public List<Vacancy> getAllByNameAndLocation(String vacancyName, String location) {
        return vacancyRepository.getAllByNameAndLocation(vacancyName.substring(1), location.substring(1));
    }

    @Override
    public Vacancy getById(long vacancyId) {
        return vacancyRepository.findById(vacancyId).orElseThrow(() -> new ResourceNotFoundException("Vacancy", "Id", vacancyId));
    }

    @Override
    public void saveAll(List<Vacancy> vacancies) {
        vacancyRepository.saveAll(vacancies);
    }

    @Override
    public void deleteAll() {
        try {
            vacancyRepository.deleteAll();
            log.debug("All vacancies data deleted");
        } catch (Exception e) {
            log.error("Error deleting vacancies data: " + e.getMessage());
        }
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        return vacancyRepository.save(vacancy);
    }

    @Override
    public long countDatabaseLinesAmount() {
        return vacancyRepository.count();
    }

    @Override
    public SkillsDto getMostPopularSkills(Optional<String> vacancyName, Optional<String> location, Optional<Integer> amount) {
        SkillsDto skillsDto = new SkillsDto();

        return null;
    }


}
