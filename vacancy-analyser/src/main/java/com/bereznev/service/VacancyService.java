package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.VacancyDto;

import java.util.Optional;

public interface VacancyService {
    public VacancyDto getAll(Optional<String> vacancyName, Optional<String> location);
}
