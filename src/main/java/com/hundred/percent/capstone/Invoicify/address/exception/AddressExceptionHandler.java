package com.hundred.percent.capstone.Invoicify.address.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AddressExceptionHandler {
    @ExceptionHandler(AddressExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    String handleAddressFoundException() {

        return "{\"message\": \"Address already exist.\"}";
    }
}
