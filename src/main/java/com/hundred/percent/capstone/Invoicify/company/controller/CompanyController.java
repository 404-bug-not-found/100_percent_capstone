package com.hundred.percent.capstone.Invoicify.company.controller;

import com.hundred.percent.capstone.Invoicify.address.exception.CompanyAddressDoesNotExistsException;
import com.hundred.percent.capstone.Invoicify.address.exception.AddressExistsException;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanyListViewDTO;
import com.hundred.percent.capstone.Invoicify.company.dto.CompanySimpleViewDTO;
import com.hundred.percent.capstone.Invoicify.company.entity.CompanyEntity;
import com.hundred.percent.capstone.Invoicify.company.exception.CompanyDoesNotExistsException;
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


    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addCompany(@RequestBody CompanyDTO companyDto) throws CompanyExistsException {

        companyService.createCompany(companyDto);

    }

    @GetMapping("/{companyname}/invoices")
    public List<InvoiceDTO> getInvoicesByCompanyName(@PathVariable String companyname) {
        return this.invoiceService.getInvoicesByCompanyName(companyname);
    }

    @GetMapping("/simpleView")
    public List<CompanySimpleViewDTO> getSimpleCompanyView() throws CompanyAddressDoesNotExistsException {

        return companyService.getSimpleCompanyView();
    }

    @GetMapping("/listView")
    public List<CompanyListViewDTO> getListCompanyView() throws CompanyAddressDoesNotExistsException {

        return companyService.getListCompanyView();
    }

    @PatchMapping("/{name}")
    public CompanyEntity updateCompany(@RequestBody CompanyEntity companyEntity, @PathVariable String name) throws AddressExistsException {
        return companyService.updateCompany(companyEntity, name);
    }

    @DeleteMapping("/{name}")
    public String deleteCompany(@PathVariable String name) throws CompanyDoesNotExistsException {
        return companyService.deleteCompany(name);

    }
}
