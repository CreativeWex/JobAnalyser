package com.bereznev.repository;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Vacancy;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    @Query(value = "SELECT v FROM Vacancy v WHERE v.name LIKE %?1% AND v.location LIKE %?2%")
    public List<Vacancy> getAllByNameAndLocation(String vacancyName, String location);
    @Query(value = "SELECT v FROM Vacancy v WHERE v.name LIKE %?1%")
    public List<Vacancy> getAllByName(String vacancyName);

    @Query(value = "DELETE FROM vacancies WHERE id >= 0", nativeQuery = true)
    public void deleteAll();

    public long count();

    @Query(value = "SELECT skills FROM vacancy_skills INNER JOIN vacancies v ON v.id = vacancy_skills.vacancy_id WHERE v.name LIKE '%' || ?1 || '%' GROUP BY skills ORDER BY COUNT(*) DESC", nativeQuery = true)
    public List<String> getMostPopularSkills(String vacancyName);

}
