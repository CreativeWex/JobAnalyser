package com.bereznev.repository;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Salary;
import com.bereznev.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    @Query("SELECT v FROM Vacancy v WHERE v.salary.minimalAmount > 0 AND v.name LIKE %?1% ORDER BY v.salary.minimalAmount ASC LIMIT 1")
    public Vacancy findFirstVacancyWithMinimalSalary(String vacancyName);

    @Query("SELECT v FROM Vacancy v WHERE v.salary.minimalAmount > 0 AND v.location LIKE %?2%  AND v.name LIKE %?1% ORDER BY v.salary.minimalAmount ASC LIMIT 1")
    public Vacancy findFirstVacancyWithMinimalSalaryByLocation(String vacancyName, String location);

    @Query("SELECT v FROM Vacancy v WHERE v.salary.maximumAmount > 0 AND v.location LIKE %?2%  AND v.name LIKE %?1% ORDER BY v.salary.minimalAmount DESC LIMIT 1")
    public Vacancy findFirstVacancyWithMaximalSalaryByLocation(String vacancyName, String location);

    @Query("SELECT v FROM Vacancy v WHERE v.salary.maximumAmount > 0  AND v.name LIKE %?1% ORDER BY v.salary.minimalAmount DESC LIMIT 1")
    public Vacancy findFirstVacancyWithMaximalSalary(String vacancyName);
}
