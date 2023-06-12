package com.bereznev.model;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "salaries")
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private BigDecimal minimalAmount;

    private BigDecimal maximumAmount;

    private String currency;

    @OneToOne(mappedBy = "salary")
    private Vacancy vacancy;

    public Salary(BigDecimal minimalAmount, BigDecimal maximumAmount, String currency) {
        this.minimalAmount = minimalAmount;
        this.maximumAmount = maximumAmount;
        this.currency = currency;
    }
}
