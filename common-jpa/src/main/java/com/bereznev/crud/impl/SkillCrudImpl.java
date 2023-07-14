package com.bereznev.crud.impl;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.crud.SkillCrud;
import com.bereznev.entity.Skill;
import com.bereznev.exceptions.logic.ResourceNotFoundException;
import com.bereznev.repository.SkillRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j
public class SkillCrudImpl implements SkillCrud {
    private final SkillRepository skillRepository;
    private static final String RESOURCE_NAME = "Skill";

    @Autowired
    public SkillCrudImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    @CachePut(value = "skills", key = "#result.id")
    public void saveAll(List<Skill> skills) {
        skillRepository.saveAll(skills);
    }

    @Override
    @CachePut(value = "skills", key = "#result.id")
    public void save(Skill skill) {
        skillRepository.save(skill);
    }

    @Override
    @CacheEvict(value = "skills")
    public void deleteAll() {
        try {
            skillRepository.deleteAll();
            log.debug("All skills data deleted");
        } catch (Exception e) {
            log.error("Error deleting skills data: " + e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "skills", key = "#id")
    public void delete(long id) {
        skillRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, "Id", id));
        skillRepository.deleteById(id);
        log.debug("deleted, id: " + id);
    }

    @Override
    public long countDatabaseLinesAmount() {
        return skillRepository.count();
    }
}
