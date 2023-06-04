package com.bereznev.vacancies.exception;
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
public class SendingUrlRequestException extends RuntimeException {

    private final String resourceName;

    public SendingUrlRequestException(String resourceName) {
        super(String.format("%s: failed to send http request'", resourceName));
        this.resourceName = resourceName;
    }
}
