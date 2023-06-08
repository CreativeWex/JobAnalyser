package com.bereznev.model;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Vacancy {

    private long id;

    private String name;

    private String description;

    @SerializedName("alternate_url")
    private String url;

    private Salary salary;

    @JsonProperty("work experience")
    private String experienceAmount;

    @JsonProperty("schedule")
    private String workSchedule;

    @JsonProperty("employment")
    private String workEmployment;

    private Employer employer;
}

