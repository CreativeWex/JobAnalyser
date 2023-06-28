package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.SkillsDto;

import java.util.Optional;

public interface SkillService {
    public SkillsDto getMostPopularSkills(Optional<String> vacancyName, Optional<String> location, Optional<Integer> amount);
}
