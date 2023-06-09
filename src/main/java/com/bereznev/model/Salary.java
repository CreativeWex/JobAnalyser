package com.bereznev.model;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salary {
    private BigDecimal from;
    private BigDecimal to;
    private String currency;
}
