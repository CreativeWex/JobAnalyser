package com.bereznev.crud.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.crud.SkillCrud;
import com.bereznev.entity.Skill;
import com.bereznev.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillCrudImpl implements SkillCrud {
    private final SkillRepository skillRepository;

    @Autowired
    public SkillCrudImpl(SkillRepository skillRepository) {
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
        return skillRepository.count();
    }
}
