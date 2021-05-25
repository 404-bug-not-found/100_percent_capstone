package com.hundred.percent.capstone.Invoicify.invoice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomInvoiceException {
    @ExceptionHandler(InvoiceWithoutCompanyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    String handleInvoiceWithoutCompanyException() {
        return "{\"Invoice cannot be created without a company.\"}";
    }

    @ExceptionHandler(UnpaidInvoiceDeleteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    String handleUnpaidInvoiceDeleteException() {
        return "{\"An Unpaid Invoice or Paid Invoice less than a year cannot be deleted.\"}";
    }

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    String handleBadInvoiceRequestException() {
        return "{\"The Invoice Object sent was a bad request.\"}";
    }
}
