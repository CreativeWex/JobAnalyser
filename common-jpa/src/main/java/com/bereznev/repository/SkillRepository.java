package com.bereznev.repository;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    @Query(value = "SELECT s FROM Skill s INNER JOIN Vacancy v ON v.id = s.vacancy.id " +
            "GROUP BY s ORDER BY COUNT(DISTINCT s) DESC")
    public List<String> getMostPopularSkills();

    @Query(value = "SELECT s FROM Skill s INNER JOIN Vacancy v ON v.id = s.vacancy.id " +
            "WHERE v.name LIKE %?1% GROUP BY s ORDER BY COUNT(DISTINCT s) DESC")
    public List<String> getMostPopularSkillsByName(String vacancyName);

    @Query(value = "SELECT s FROM Skill s INNER JOIN Vacancy v ON v.id = s.vacancy.id " +
            "WHERE v.location LIKE %?1% GROUP BY s ORDER BY COUNT(DISTINCT s) DESC")
    public List<String> getMostPopularSkillsByLocation(String location);

    @Query(value = "SELECT s FROM Skill s INNER JOIN Vacancy v ON v.id = s.vacancy.id " +
            "WHERE v.name LIKE %?1% AND v.location LIKE %?2% GROUP BY s ORDER BY COUNT(DISTINCT s) DESC")
    public List<String> getMostPopularSkillsByNameAndLocation(String vacancyName, String location);
}
