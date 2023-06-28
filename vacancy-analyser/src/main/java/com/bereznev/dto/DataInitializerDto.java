package com.bereznev.dto;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DataInitializerDto {
    private String status;

    private String description;

    @JsonProperty("date_time")
    private LocalDateTime localDateTime;

    @JsonProperty("time_spent")
    private String timeSpent;
}
