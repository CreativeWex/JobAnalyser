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
    @Query(value = "select e from Employer e join e.vacancies v where v.name like %?1%")
    public List<Employer> getAllByVacancyName(String vacancyName);

    @Query(value = "select e from Employer e where e.location like %?1%")
    public List<Employer> getAllByLocation(String location);
    @Query(value = "select e from Employer e join e.vacancies v where v.name LIKE %?1% and e.location like %?2%")
    public List<Employer> getAllByVacancyNameAndLocation(String vacancyName, String location);

    @Query(value = "delete from employers where id >= 0", nativeQuery = true)
    public void deleteAll();
}
