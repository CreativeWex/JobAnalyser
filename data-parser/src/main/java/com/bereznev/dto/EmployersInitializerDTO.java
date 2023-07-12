package com.bereznev.dto;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.Dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployersInitializerDTO extends Dto {
    private String status;

    private String description;

    @JsonProperty("pages_amount")
    private Integer pagesAmount;

    @JsonProperty("date_time")
    private LocalDateTime localDateTime;
}
