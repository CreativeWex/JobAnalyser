package com.bereznev.dto.employers;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.model.Employer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDTO {

    @JsonProperty("found")
    private int employersNumber;

    @JsonProperty("items")
    private Set<Employer> employers;
}
