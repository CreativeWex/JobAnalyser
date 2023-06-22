package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.SkillsDto;
import com.bereznev.entity.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillService {
    public void saveAll(List<Skill> skills);
    public void save(Skill skill);

    public void deleteAll();

    public long countDatabaseLinesAmount();

    public SkillsDto getMostPopularSkills(Optional<String> vacancyName, Optional<String> location, Optional<Integer> amount);
}
