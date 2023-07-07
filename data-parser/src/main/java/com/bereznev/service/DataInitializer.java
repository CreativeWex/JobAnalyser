package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.DataInitializerDTO;

import java.util.Optional;

public interface DataInitializer {
    public DataInitializerDTO refreshData(Optional<String> vacancyName, Optional<String> location, Optional<Integer> pagesAmount);
    public void deleteAllData();
}
