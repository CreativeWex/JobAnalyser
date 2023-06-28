package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.SkillsDto;
import com.bereznev.repository.SkillRepository;
import com.bereznev.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public SkillsDto getMostPopularSkills(Optional<String> vacancyName, Optional<String> location, Optional<Integer> amount) {
        SkillsDto skillsDto = new SkillsDto();
        final long startTime = System.currentTimeMillis();
        String vacancyNameTrimmed = "";
        String locationTrimmed = "";

        if (vacancyName.isPresent()) {
            skillsDto.setNameFilter(vacancyName.get());
            vacancyNameTrimmed = vacancyName.get().substring(1);
        }
        if (location.isPresent()) {
            skillsDto.setLocationFilter(location.get());
            locationTrimmed = location.get().substring(1);
        }

        List<String> popularSkills;
        if (vacancyName.isPresent() && location.isPresent()) {
            popularSkills = skillRepository.getMostPopularSkillsByNameAndLocation(vacancyNameTrimmed, locationTrimmed);
        } else if (vacancyName.isPresent()) {
            popularSkills = skillRepository.getMostPopularSkillsByName(vacancyNameTrimmed);
        } else if (location.isPresent()) {
            popularSkills = skillRepository.getMostPopularSkillsByLocation(locationTrimmed);
        } else {
            popularSkills = skillRepository.getMostPopularSkills();
        }

        if (amount.isPresent()) {
            skillsDto.setSkills(popularSkills.subList(0, Math.min(amount.get(), popularSkills.size())));
        } else {
            skillsDto.setSkills(popularSkills);
        }

        skillsDto.setTimeSpent(System.currentTimeMillis() - startTime + "ms");
        return skillsDto;
    }
}
