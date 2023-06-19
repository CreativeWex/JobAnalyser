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

public class ErrorDTO {
    private String status = "error";

    @JsonProperty("date_time")
    private LocalDateTime localDateTime;

    @JsonProperty("description")
    private String exceptionMessage;

    @JsonProperty("path")
    private String endpoint;
}
