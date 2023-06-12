package com.bereznev.model;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "vacancies")
public class Vacancy {

    @Id
    private long id;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @SerializedName("alternate_url")
    @Column(columnDefinition = "TEXT")
    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salary_id")
    private Salary salary;

    @JsonProperty("work experience")
    @Column(name = "experience_amount", nullable = false)
    private String experienceAmount;

    @JsonProperty("schedule")
    @Column(name = "schedule", nullable = false)
    private String workSchedule;

    @JsonProperty("employment")
    @Column(name = "employment", nullable = false)
    private String workEmployment;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;
}

