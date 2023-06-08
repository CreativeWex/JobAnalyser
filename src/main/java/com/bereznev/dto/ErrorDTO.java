package com.bereznev.dto;
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

public class ErrorDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime timestamp;

    @JsonProperty("status")
    public static int responseCode;

    @JsonProperty("description")
    private String exceptionMessage;

    @JsonProperty("path")
    private String endpoint;

    @JsonProperty("evoked from")
    public static String resourceName;
}
