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
    @Query(value = "DELETE FROM salaries where id >= 0", nativeQuery = true)
    public void deleteAll();

    @Query("SELECT v FROM Vacancy v WHERE v.salary.minimalAmount > 0 ORDER BY v.salary.minimalAmount ASC")
    public Vacancy findFirstVacancyWithMinimalSalary();

    @Query("SELECT v FROM Vacancy v WHERE v.salary.minimalAmount > 0 AND v.location LIKE %?1% ORDER BY v.salary.minimalAmount ASC")
    public Vacancy findFirstVacancyWithMinimalSalaryByLocation(String location);

    @Query("SELECT v FROM Vacancy v WHERE v.salary.maximumAmount > 0 AND v.location LIKE %?1% ORDER BY v.salary.minimalAmount DESC")
    public Vacancy findFirstVacancyWithMaximalSalaryByLocation(String location);

    @Query("SELECT v FROM Vacancy v WHERE v.salary.maximumAmount > 0 ORDER BY v.salary.minimalAmount DESC")
    public Vacancy findFirstVacancyWithMaximalSalary();

    long count();
}
