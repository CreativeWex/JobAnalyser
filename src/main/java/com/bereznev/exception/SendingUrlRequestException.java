package com.bereznev.exception;
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
    private final Exception exception;

    public SendingUrlRequestException(String resourceName, int responseCode, Exception exception) {
        super(String.format("%s: failed to send http request'", resourceName));
        this.resourceName = resourceName;
        this.responseCode = responseCode;
        this.exception = exception;
    }
}
