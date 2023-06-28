package com.bereznev;

/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
*/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DataParserApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataParserApplication.class, args);
    }
}
