package com.bereznev.entity;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "salaries")
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private BigDecimal minimalAmount;

    private BigDecimal maximumAmount;

    private String currency;

    @JsonBackReference
    @OneToOne(mappedBy = "salary")
    private Vacancy vacancy;

    public Salary(BigDecimal minimalAmount, BigDecimal maximumAmount, String currency) {
        this.minimalAmount = minimalAmount;
        this.maximumAmount = maximumAmount;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "minimalAmount=" + minimalAmount +
                ", maximumAmount=" + maximumAmount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
