package com.bereznev.repository;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {
    @Query("select e from Employer e where e.name = ?1 and e.location=?2")
    Optional<Employer> findEmployerByNameAnAndLocation(String name, String location);

    @Query(value = "delete from employers where id >= 0", nativeQuery = true)
    void deleteAll();

    long count();
}
