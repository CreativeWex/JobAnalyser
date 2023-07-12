package com.bereznev.dao;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Employer;
import com.bereznev.repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerDao {
    private final EmployerRepository employerRepository;

    @Autowired
    public EmployerDao(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    public List<Employer> getAllNotLikeVacancyName(String vacancyName) {
        return employerRepository.getAllNotLikeVacancyName(vacancyName);
    }
}
