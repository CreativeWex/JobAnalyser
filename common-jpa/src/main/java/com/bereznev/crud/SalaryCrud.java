package com.bereznev.crud;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Salary;

import java.util.List;

public interface SalaryCrud {
    public void deleteAll();
    public Salary save(Salary salary);
    public long countDatabaseLinesAmount();
    public void saveAll(List<Salary> salaries);
}
