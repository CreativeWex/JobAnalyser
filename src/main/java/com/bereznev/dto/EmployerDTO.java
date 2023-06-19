package com.bereznev.dto;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Employer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployerDTO {
    @JsonProperty("sorted_by_vacancy")
    private String vacancyFilter;

    @JsonProperty("found")
    private int employersNumber;

    @JsonProperty("items")
    private Set<Employer> employers;
}
