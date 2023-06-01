package com.bereznev.vacancies.entity;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Vacancy {
    private String id;
    private Employer employer;
}
