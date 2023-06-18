package com.bereznev.repository;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    @Query(value = "select v from Vacancy v where v.name LIKE %?1% and v.location LIKE %?2%")
    public List<Vacancy> getAllByNameAndLocation(String vacancyName, String location);
    @Query(value = "select v from Vacancy v where v.name LIKE %?1%")
    public List<Vacancy> getAllByName(String vacancyName);

    @Query(value = "delete from vacancies where id >= 0", nativeQuery = true)
    public void deleteAll();

    public long count();
}
