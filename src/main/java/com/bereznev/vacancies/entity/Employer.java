package com.bereznev.vacancies.entity;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Employer {
    private long id;

    private String name;

    @SerializedName("alternate_url")
    private String url;

    private String description;

    @SerializedName("open_vacancies")
    private int openVacancies;
}
