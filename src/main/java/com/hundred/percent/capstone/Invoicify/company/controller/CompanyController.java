package com.hundred.percent.capstone.Invoicify.company.controller;

import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyListViewDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanySimpleViewDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyExistsException;
import com.hundred.percent.capstone.Invoicify.company.service.CompanyService;
import com.hundred.percent.capstone.Invoicify.invoice.dto.InvoiceDTO;
import com.hundred.percent.capstone.Invoicify.invoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    CompanyService companyService;
    @Autowired
    InvoiceService invoiceService;

    @GetMapping
    public List<CompanyEntity> getCompanies() {

        return companyService.getAllCompanies();
    }


    @PostMapping("/addCompany")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addCompany(@RequestBody CompanyDTO companyDto) throws CompanyExistsException {

        companyService.createCompany(companyDto);

    }

    @GetMapping("/{companyname}/invoices")
    public List<InvoiceDTO> getInvoicesByCompanyName(@PathVariable String companyname){
        return this.invoiceService.getInvoicesByCompanyName(companyname);
    }

    @GetMapping("/simpleView")
    public List<CompanySimpleViewDTO> getSimpleCompanyView() {

        return companyService.getSimpleCompanyView();
    }

    @GetMapping("/listView")
    public List<CompanyListViewDTO> getListCompanyView() {

        return companyService.getListCompanyView();
    }
}
