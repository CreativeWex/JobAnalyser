package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.dto.DataParserDTO;

import java.util.Optional;

public interface EmployerInitializer {
    public DataParserDTO initData(Optional<String> vacancyName, Optional<String> location, Optional<Integer> pagesAmount);
}
