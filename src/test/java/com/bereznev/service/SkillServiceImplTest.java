package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.SkillsDto;
import com.bereznev.entity.Skill;
import com.bereznev.repository.SkillRepository;
import com.bereznev.service.impl.SkillServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SkillServiceImplTest {
    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillServiceImpl skillService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveAll_ShouldCallSkillRepositorySaveAll() {
        List<Skill> skills = Arrays.asList(new Skill("Java"), new Skill("Python"));

        skillService.saveAll(skills);

        verify(skillRepository).saveAll(skills);
    }

    @Test
    void save_ShouldCallSkillRepositorySave() {
        Skill skill = new Skill("Java");

        skillService.save(skill);

        verify(skillRepository).save(skill);
    }

    @Test
    void deleteAll_ShouldCallSkillRepositoryDeleteAll() {
        skillService.deleteAll();

        verify(skillRepository).deleteAll();
    }

    @Test
    void countDatabaseLinesAmount_ShouldReturnSkillRepositoryCount() {
        long expectedCount = 5;
        when(skillRepository.count()).thenReturn(expectedCount);

        long actualCount = skillService.countDatabaseLinesAmount();

        assertEquals(expectedCount, actualCount);
    }

    @Test
    void getMostPopularSkills_WithVacancyNameAndLocation_ShouldReturnSkillsDtoWithFilteredSkills() {
        String vacancyName = "Software Developer";
        String location = "New York";
        List<String> expectedSkills = Arrays.asList("Java", "Python");
        when(skillRepository.getMostPopularSkillsByNameAndLocation(vacancyName, location)).thenReturn(expectedSkills);

        SkillsDto skillsDto = skillService.getMostPopularSkills(Optional.of(vacancyName), Optional.of(location), Optional.empty());

        assertEquals(vacancyName, skillsDto.getNameFilter());
        assertEquals(location, skillsDto.getLocationFilter());
        assertEquals(expectedSkills, skillsDto.getSkills());
    }

    @Test
    void getMostPopularSkills_WithVacancyName_ShouldReturnSkillsDtoWithFilteredSkills() {
        String vacancyName = "Software Developer";
        List<String> expectedSkills = Arrays.asList("Java", "Python");
        when(skillRepository.getMostPopularSkillsByName(vacancyName)).thenReturn(expectedSkills);

        SkillsDto skillsDto = skillService.getMostPopularSkills(Optional.of(vacancyName), Optional.empty(), Optional.empty());

        assertEquals(vacancyName, skillsDto.getNameFilter());
        assertEquals(Collections.emptyList(), skillsDto.getLocationFilter());
        assertEquals(expectedSkills, skillsDto.getSkills());
    }

    @Test
    void getMostPopularSkills_WithLocation_ShouldReturnSkillsDtoWithFilteredSkills() {
        String location = "New York";
        List<String> expectedSkills = Arrays.asList("Java", "Python");
        when(skillRepository.getMostPopularSkillsByLocation(location)).thenReturn(expectedSkills);

        SkillsDto skillsDto = skillService.getMostPopularSkills(Optional.empty(), Optional.of(location), Optional.empty());

        assertEquals(Collections.emptyList(), skillsDto.getNameFilter());
        assertEquals(location, skillsDto.getLocationFilter());
        assertEquals(expectedSkills, skillsDto.getSkills());
    }

    @Test
    void getMostPopularSkills_WithoutFilters_ShouldReturnSkillsDtoWithAllSkills() {
        List<String> expectedSkills = Arrays.asList("Java", "Python");
        when(skillRepository.getMostPopularSkills()).thenReturn(expectedSkills);

        SkillsDto skillsDto = skillService.getMostPopularSkills(Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(Collections.emptyList(), skillsDto.getNameFilter());
        assertEquals(Collections.emptyList(), skillsDto.getLocationFilter());
        assertEquals(expectedSkills, skillsDto.getSkills());
    }
}