package com.bereznev.exceptions.logic;
/*
    =====================================
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
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
