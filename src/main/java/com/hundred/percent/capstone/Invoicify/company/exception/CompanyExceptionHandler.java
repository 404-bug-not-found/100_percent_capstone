package com.hundred.percent.capstone.Invoicify.company.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CompanyExceptionHandler {
    @ExceptionHandler(CompanyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    String handleCompanyNameFoundException() {
        return "{\"message\": \"Company already exist.\"}";
    }

    @ExceptionHandler(CompanyDoesNotExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    String handleCompanyNotFoundException() {
        return "{\"message\": \"Company does not exist.\"}";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    String invalidCompanyDTO() {
        return "{\"message\": \"One or more inputs are missing from the request.\"}";
    }
}
