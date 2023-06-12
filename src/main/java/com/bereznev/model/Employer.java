package com.bereznev.model;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.List;

@Data
@NoArgsConstructor

@Entity
@Table(name = "employers")
public class Employer {

    @Id
    private long id;

    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String location;

    @SerializedName("alternate_url")
    @Column(columnDefinition = "TEXT")
    private String url;

    @Column(columnDefinition = "TEXT")
    private String description;

    @SerializedName("open_vacancies")
    @Column(name = "open_vacancies_amount")
    private int openVacanciesAmount;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vacancy> vacancies;
}
