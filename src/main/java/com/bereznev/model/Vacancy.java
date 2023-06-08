package com.bereznev.model;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

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

    @SerializedName("alternate_url")
    private String url;

    private Salary salary;

    @SerializedName("experience")
    private String experienceAmount;

    private Employer employer;
}

