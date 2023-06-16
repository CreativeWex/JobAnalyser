package com.bereznev.service;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import java.util.Optional;

public interface DataInitializer {
    public void initData(Optional<String> vacancyName);
    public void deleteAllData();
}
