package com.bereznev.clients.exception;
/*
    =====================================
    @project ClientMicroservice
    @author Bereznev Nikita @CreativeWex
    =====================================
 */

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ExceptionConverter {
    public static String convertErrorsToString(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError error: bindingResult.getFieldErrors()) {
            errorMessage.append(error.getField()).append(':').append(error.getDefaultMessage()).append("; ");
        }
        return errorMessage.toString();
    }
}


