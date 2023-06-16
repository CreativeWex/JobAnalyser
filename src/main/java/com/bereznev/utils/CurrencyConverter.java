package com.bereznev.utils;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import com.bereznev.entity.Salary;
import com.bereznev.entity.Vacancy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;

@Slf4j
public class CurrencyConverter {

    private static final String API_URL = "https://www.cbr-xml-daily.ru/daily_json.js";

    private CurrencyConverter() {
    }

    public static void convertCurrency(Vacancy vacancy) {
        String currencyCode = vacancy.getSalary().getCurrency();
        BigDecimal startPrice = vacancy.getSalary().getMinimalAmount();
        BigDecimal finishPrice = vacancy.getSalary().getMaximumAmount();
        BigDecimal rate = BigDecimal.valueOf(getCurrencyRate(currencyCode));
        BigDecimal convertedStartPrice = startPrice.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal convertedFinishPrice = finishPrice.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        vacancy.setSalary(new Salary(convertedStartPrice, convertedFinishPrice, "RUR"));
    }

    private static double getCurrencyRate(String currencyCode) {
        try {
            URL url = new URL(API_URL);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(url);
            JsonNode currencyNode = rootNode.path("Valute").path(currencyCode);
            double rate = currencyNode.path("Value").asDouble();
            int nominal = currencyNode.path("Nominal").asInt();
            return rate / nominal;
        } catch (IOException e) {
            throw new RuntimeException("Error getting currency rate", e);
        }
    }
}








