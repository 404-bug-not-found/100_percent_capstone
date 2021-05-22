package com.hundred.percent.capstone.Invoicify.invoice.controller;


import com.hundred.percent.capstone.Invoicify.invoice.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.invoice.dto.ItemDTO;
import com.hundred.percent.capstone.Invoicify.invoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;

    @GetMapping
    public List<InvoiceDTO> getAllInvoices(){
        return invoiceService.getAllInvoices();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createAnInvoice(@RequestBody InvoiceDTO invoiceDTO){
        return new ResponseEntity<String>("Invoice ID created was " + this.invoiceService.createInvoice(invoiceDTO).toString(),HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public List<InvoiceDTO> getAnInvoicebyId(@PathVariable String id){
            return this.invoiceService.getInvoiceById(Long.parseLong(id));
    }
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void  updateInvoice(@RequestBody InvoiceDTO invoiceDTO,@PathVariable String id){
         this.invoiceService.updateInvoice(Long.parseLong(id),invoiceDTO);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  deleteInvoice(@PathVariable String id){
        this.invoiceService.deleteInvoice(Long.parseLong(id));
    }

}
