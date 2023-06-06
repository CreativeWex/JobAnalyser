package com.bereznev.vacancies.model.json_response;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime timestamp;

    @JsonProperty("status")
    private int responseCode;

    @JsonProperty("description")
    private String exceptionMessage;

    @JsonProperty("path")
    private String endpoint;

    @JsonProperty("evoked from")
    private String resourceName;
}
