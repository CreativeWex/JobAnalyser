package com.bereznev.repository;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    @Query(value = "DELETE FROM employers WHERE id >= 0", nativeQuery = true)
    public void deleteAll();

    @Query(value = "SELECT e FROM Employer e JOIN e.vacancies v WHERE v.name LIKE %:vacancyName%")
    public List<Employer> getAllByVacancyName(String vacancyName);

    @Query(value = "SELECT e FROM Employer e JOIN e.vacancies v WHERE v.name LIKE %?1% and e.location = ?2")
    public List<Employer> getAllByVacancyNameAndLocation(String vacancyName, String location);
}
