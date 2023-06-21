package com.bereznev.exception;
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
@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class DataInitialisationException extends RuntimeException {
    private final String resourceName;
    private final String reason;

    public DataInitialisationException(String resourceName, String reason) {
        super(String.format("Data initialisation exception in %s: '%s'", resourceName, reason));
        this.resourceName = resourceName;
        this.reason = reason;
    }
}
