package com.bereznev.crud;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Skill;

import java.util.List;

public interface SkillCrud {
    public void saveAll(List<Skill> skills);
    public void save(Skill skill);
    public void deleteAll();
    public long countDatabaseLinesAmount();
}
