package com.bereznev.clients.exception;
/*
    =====================================
    @project ClientsMicroservice
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class BadArgumentsException extends RuntimeException {

    private final String resourceName;
    private final String errorMessage;

    public BadArgumentsException(String resourceName, String errorMessage) {
        super(String.format("%s bad arguments : %s", resourceName, errorMessage));
        this.resourceName = resourceName;
        this.errorMessage = errorMessage;
    }
}
