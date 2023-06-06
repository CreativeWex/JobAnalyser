package com.bereznev.vacancies.exception;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import lombok.Getter;

import java.io.IOException;

@Getter
public class SendingUrlRequestException extends RuntimeException {

    private final String resourceName;
    private final int responseCode;
    private final IOException exception;

    public SendingUrlRequestException(String resourceName, int responseCode, IOException exception) {
        super(String.format("%s: failed to send http request'", resourceName));
        this.resourceName = resourceName;
        this.responseCode = responseCode;
        this.exception = exception;
    }
}
