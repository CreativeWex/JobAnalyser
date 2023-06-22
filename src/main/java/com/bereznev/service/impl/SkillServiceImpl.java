package com.bereznev.service.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Skill;
import com.bereznev.repository.SkillRepository;
import com.bereznev.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public void saveAll(List<Skill> skills) {
        skillRepository.saveAll(skills);
    }

    @Override
    public void save(Skill skill) {
        skillRepository.save(skill);
    }

    @Override
    public void deleteAll() {
        skillRepository.deleteAll();
    }

    @Override
    public long countDatabaseLinesAmount() {
        return 0;
    }
}
