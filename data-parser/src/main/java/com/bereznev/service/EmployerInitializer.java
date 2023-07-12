package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.EmployersInitializerDTO;

import java.util.Optional;

public interface EmployerInitializer {
    public EmployersInitializerDTO initData(Optional<String> vacancyName, Optional<String> location, Optional<Integer> pagesAmount);
}
