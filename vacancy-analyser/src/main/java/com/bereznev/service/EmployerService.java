package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.EmployerDto;

import java.util.Optional;

public interface EmployerService {
    public EmployerDto getAll(Optional<String> vacancyName, Optional<String> location);
    public String compareTo(long firstId, long secondId); //TODO
//    public List<Employer> getEmployersWithMostOpenVacancies();
}
