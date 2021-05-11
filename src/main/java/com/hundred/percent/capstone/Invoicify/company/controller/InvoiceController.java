package com.hundred.percent.capstone.Invoicify.company.controller;


import com.hundred.percent.capstone.Invoicify.company.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.company.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @GetMapping
    public List<InvoiceDTO> getInvoices(){

        return invoiceService.getAllInvoice();
    }


}
