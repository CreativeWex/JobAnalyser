package com.bereznev.repository;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.model.Employer;
import com.bereznev.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    @Query(value = "delete from vacancies where id >= 0", nativeQuery = true)
    void deleteAll();

    @Query("select v from Vacancy v where v.name = ?1 and v.location = ?2 and v.employer.id = ?3")
    Optional<Vacancy> findVacanciesByNameAndLocationAndEmployerId(String name, String location, long employerId);

    long count();
}
